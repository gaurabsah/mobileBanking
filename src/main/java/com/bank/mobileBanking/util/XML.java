package com.bank.mobileBanking.util;

import com.bank.mobileBanking.dto.TransactionDTO;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class XML {

    public byte[] createXml(List<TransactionDTO> transactions) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JAXBContext jaxbContext = JAXBContext.newInstance(TransactionDTO.class); // Create a wrapper for the list
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        TransactionDTO transactionList = new TransactionDTO(transactions);
        marshaller.marshal(transactionList, outputStream);
        return outputStream.toByteArray();
    }

}
