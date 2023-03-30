package com.su.zwk.swing.ui;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;

@Component
public class MainFrame extends JFrame {

    @PostConstruct
    public void init(){
        this.setTitle("ZWK");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
