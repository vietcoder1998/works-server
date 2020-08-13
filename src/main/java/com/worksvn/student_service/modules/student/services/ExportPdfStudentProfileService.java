package com.worksvn.student_service.modules.student.services;

import com.itextpdf.text.DocumentException;
import com.worksvn.common.modules.common.responses.DownloadUrlDto;
import com.worksvn.common.services.pdf.PdfExportService;
import com.worksvn.common.services.pdf.models.PdfExportConfig;
import com.worksvn.common.services.thymeleaf.HtmlTemplateBindingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

@Service
public class ExportPdfStudentProfileService {
    @Autowired
    private PdfExportService pdfExportService;

    @Autowired
    private HtmlTemplateBindingService htmlTemplateBindingService;

    public DownloadUrlDto exportPdfStudentProfile(String studentID, String templateName) throws IOException, DocumentException {
        byte[] data = pdfExportService.exportPdf(templateName, new HashMap<>(), new PdfExportConfig());
        FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/dev/data.pdf");
        fileOutputStream.write(data);
        fileOutputStream.close();

//        String html = htmlTemplateBindingService.bindTemplate(templateName, new HashMap<>());

//        ITextRenderer renderer = new ITextRenderer();
//        renderer.getFontResolver().addFont("src/main/resources/fonts/brushsbi.ttf", "UTF-8", BaseFont.EMBEDDED);
//        renderer.setDocumentFromString(html);
//        renderer.layout();
//        renderer.createPDF(fileOutputStream);
//        renderer.finishPDF();
//        fileOutputStream.close();

        return new DownloadUrlDto();
    }
}
