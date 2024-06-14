package org.example;
public class Converter {
    public EmploymentRequest toEmploymentRequest(ViewModel viewModel){
        return new EmploymentRequest(
            viewModel.getName(),
            viewModel.getPosition(),
            viewModel.getAnnualSalary());
    }
    
}
