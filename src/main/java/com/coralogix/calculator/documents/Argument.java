package com.coralogix.calculator.documents;

import com.mongodb.lang.NonNull;
import com.sun.istack.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "arguments")
public class Argument {

    private int number;
    @Id
    private String index;

    public Argument(){
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getIndex() {
        return index;
    }
    public void setIndex(String index) {
        this.index = index;
    }
}
