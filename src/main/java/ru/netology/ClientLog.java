package ru.netology;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {
  List<logObject> log = new ArrayList<>();

  public ClientLog() {}

  public void log(int productName, long amount) {
    log.add(new logObject(productName, amount));
  }

  public void exportAsCSV(File txtFile) throws IOException {
    ColumnPositionMappingStrategy<logObject> strategy =
      new ColumnPositionMappingStrategy<>();
    strategy.setType(logObject.class);
    strategy.setColumnMapping("productName", "amount");

    boolean writeHeader = !txtFile.exists() || txtFile.length() == 0;

    try(Writer writer = new FileWriter( txtFile, true)) {
      if(writeHeader) writer.write("productName,amount\n");
      StatefulBeanToCsv<logObject> sbc =
        new StatefulBeanToCsvBuilder<logObject>(writer)
          .withApplyQuotesToAll(false)
          .withMappingStrategy(strategy)
          .build();
      sbc.write(log);
    } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
      e.printStackTrace();
    }
  }

  public class logObject {
    int productName;
    long amount;

    public logObject(int productName, long amount) {
      this.productName = productName;
      this.amount = amount;
    }

    @Override
    public String toString() {
      return "logObject{" +
        "productName='" + productName + '\'' +
        ", amount='" + amount + '\'' +
        '}';
    }
  }
}
