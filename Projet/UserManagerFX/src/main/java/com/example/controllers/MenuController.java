package com.example.controllers;

import javafx.fxml.FXML;
import com.example.App;

public class MenuController {
    
    @FXML
    private void openUserList() {
        App.switchScene("MainView.fxml");
    }
    
    @FXML
    private void openSearchUser() {
        App.switchScene("SearchView.fxml");
    }
    
    @FXML
    private void logout() {
        App.switchScene("LoginView.fxml");
    }
}
