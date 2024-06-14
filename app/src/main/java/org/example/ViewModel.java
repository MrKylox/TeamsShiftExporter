package org.example;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ViewModel {

    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty position = new SimpleStringProperty("");
    private final DoubleProperty annualSalary = new SimpleDoubleProperty();

    private final Converter converter = new Converter();

    private final Model model = new Model();

    public String getName(){
        return name.get();
    }

    public StringProperty nameProperty(){
        return name;
    }

    public void setName(String name){
        this.name.set(name);
    }

    public String getPosition(){
        return position.get();
    }

    public StringProperty positionProperty(){
        return position;
    }

    public void setPosition(String position){
        this.position.set(position);
    }

    public double getAnnualSalary(){
        return annualSalary.get();
    }

    public DoubleProperty annualSalaryProperty(){
        return annualSalary;
    }

    public void setAnnualSalary(double annualSalary){
        this.annualSalary.set(annualSalary);
    }

    public void save(){
        EmploymentRequest data = converter.toEmploymentRequest(this);
        model.save(data);
    }

    public void reset(){
        this.name.set("");
        this.position.set("");
        this.annualSalary.set(0.0d);
    }

}
