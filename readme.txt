Klasifikator dokumentu

This application serves as a document classificator.

Input is scanned document. Output is info about the document.

It uses tesseract for OCR. App uses Tesseract as an external process. Tesseract must be installed on the system and added to path.

You can set environmental ocr.tesseract.path to add path to the Tesseract e.g. Run JVM with -Docr.tesseract.path=C:\apps\tesseract-folder\tesseract.

You can also use tess4j - set ocr.usetTess4j=true