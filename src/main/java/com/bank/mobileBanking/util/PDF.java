package com.bank.mobileBanking.util;

import com.bank.mobileBanking.dto.TransactionDTO;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class PDF {

    public byte[] createPdf(List<TransactionDTO> transactions) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        for (TransactionDTO transaction : transactions) {
            document.add(new Paragraph(transaction.toString())); // Customize as needed
        }

        document.close();
        return outputStream.toByteArray();
    }

}
