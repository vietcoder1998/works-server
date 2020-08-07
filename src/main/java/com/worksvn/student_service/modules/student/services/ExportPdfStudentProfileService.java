package com.worksvn.student_service.modules.student.services;

import com.itextpdf.text.DocumentException;
import com.worksvn.common.modules.common.responses.DownloadUrlDto;
import com.worksvn.common.services.pdf.PdfExportService;
import com.worksvn.common.services.pdf.models.PdfExportConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

@Service
public class ExportPdfStudentProfileService {
    @Autowired
    private PdfExportService pdfExportService;

    public DownloadUrlDto exportPdfStudentProfile(String studentID, String templateName) throws IOException, DocumentException {
        byte[] data = pdfExportService.exportPdf(templateName, new HashMap<>(), new PdfExportConfig());
        FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/dev/data.pdf");
        fileOutputStream.write(data);
        fileOutputStream.close();
        return new DownloadUrlDto();
    }
}
