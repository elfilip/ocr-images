package org.elias.gui;

import org.elias.entity.ProcessResult;
import org.elias.entity.ResultTuple;
import org.elias.ocr.TesseractOCR;
import org.elias.ocr.TesseractProcess;
import org.elias.processor.FileProcessor;
import org.elias.processor.SemanticProcessor;
import org.elias.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Graphic GUI for the APP
 */
@Component
public class FileManager {

    private Logger logger = LoggerFactory.getLogger(FileManager.class);

    private JButton addFileButon;
    private JPanel mainPanel;
    private JButton processButton;
    private JTextField pathText;
    private JPanel panelImage;
    private JLabel labelImage;
    private JPanel panelList;
    private JTable tableList;
    private JLabel labelInProgress;
    private JButton buttonDeleteAll;

    private File currentFile = null;

    @Autowired
    private FileService fileService;

    @Autowired
    private DialogManager dialogManager;

    @Autowired
    private TesseractOCR ocr;

    private Map<String, Integer> columns = new LinkedHashMap<>();
    private int columnCount = 0;

    private AtomicInteger fileInProgress = new AtomicInteger(0);


    public FileManager() {
        addFileButon.addActionListener(actionEvent -> {
            logger.debug("Click select file");
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
            int rVal = chooser.showOpenDialog(mainPanel);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                currentFile = chooser.getSelectedFile();
                pathText.setText(currentFile.getAbsolutePath());
                try {
                    displayImage(currentFile);
                } catch (IOException e) {
                    dialogManager.showError("Can't load image: " + e.getMessage());
                    logger.error("can't load image: ", e);
                }
                logger.debug("Selected file: {}", currentFile.getAbsolutePath());
            }
        });
        createTable();

        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                logger.debug("User clicked on processing file: {}", currentFile != null ? currentFile.getAbsolutePath() : "NULL");
                if (currentFile == null) {
                    dialogManager.showError("No file selected to process.");
                    logger.error("No file selected to process");
                    return;
                }
                labelInProgress.setVisible(true);
                fileInProgress.incrementAndGet();
                fileService.processFile(currentFile);
            }
        });
        buttonDeleteAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                logger.debug("User clicked on delete all");
                deleteAllData();
            }
        });
    }

    public void init() {
        logger.debug("Initiating application");
        fileService.registerProcessor(new FileProcessor());
        fileService.registerProcessor(new SemanticProcessor(ocr));
        List<ProcessResult> data = fileService.loadData();
        if (data != null) {
            data.forEach(pres -> addRow(pres));
        }
        processResults();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private void displayImage(File path) throws IOException {
        logger.debug("Dispaying image: {}", path.getAbsolutePath());
        BufferedImage documentImage = ImageIO.read(path);
        if (documentImage == null) {
            dialogManager.showError("The file is not an image!");
        }
        int height = panelImage.getHeight();
        int width = (int) (documentImage.getWidth() * ((double) height / (double) documentImage.getHeight()));
        Image dimg = documentImage.getScaledInstance(width, height,
                Image.SCALE_SMOOTH);
        labelImage.setIcon(new ImageIcon(dimg));
    }

    private void createTable() {
        TableModel tableModel = new DefaultTableModel(new String[]{}, 0);
        tableList.setModel(tableModel);
    }

    private synchronized void addRow(ProcessResult processResult) {
        DefaultTableModel tableModel = (DefaultTableModel) tableList.getModel();
        for (ResultTuple tuple : processResult.getResults()) {
            if (!columns.containsKey(tuple.getName())) {
                columns.put(tuple.getName(), columnCount++);
                tableModel.addColumn(tuple.getName());
            }
        }
        Object[] data = new Object[columnCount];
        for (ResultTuple tuple : processResult.getResults()) {
            data[columns.get(tuple.getName())] = tuple.getValue();
        }
        tableModel.addRow(data);
        logger.debug("Added new row with processing results: {}", processResult.getFilename());
    }

    private synchronized void deleteAllData() {
        columnCount = 0;
        columns.clear();
        DefaultTableModel tableModel = (DefaultTableModel) tableList.getModel();
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        fileService.deleteAll();
        logger.debug("All data deleted");
    }

    /**
     * Process results as they are available. Results are processed in one thread.
     */
    private void processResults() {
        Thread resExtractor = new Thread(() -> {
            while (true) {
                try {
                    ProcessResult res = null;
                    try {
                        res = fileService.getOneProcessResult();
                        logger.debug("Getting new result for {}", res.getFilename());
                    } catch (ExecutionException e) {
                        logger.error("Can't get image results: ", e);
                        dialogManager.showError("Error when processing image: " + e.getMessage());
                    }
                    if(res == null){
                        dialogManager.showError("Image processing failed");
                    }else {
                        addRow(res);
                    }
                    if (fileInProgress.decrementAndGet() == 0) {
                        labelInProgress.setVisible(false);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        resExtractor.setDaemon(true);
        resExtractor.start();
    }
}
