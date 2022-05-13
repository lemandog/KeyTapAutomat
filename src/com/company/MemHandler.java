package com.company;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

public class MemHandler {
    static Vector<BlocksHandler> blocksStashLoad;
    static File loader;
    static final DateTimeFormatter sdfF = DateTimeFormatter.ofPattern("dd MM yyyy HH mm ss");
    static JFrame main;
    public static void getSaveLoadUI(){
        main = null;
        main = new JFrame("Load/save");
        main.setSize(400,200);
        main.setLayout(new BorderLayout());
        JPanel saveLoadActionPanel = new JPanel();
        saveLoadActionPanel.setBackground(Color.GRAY);
        saveLoadActionPanel.setLayout(new GridLayout(1,2));
        JTextField setupName = new JTextField();
        setupName.setName("Name of sequence");

        JButton save = new JButton("Save");
        save.addActionListener((e) -> saveLayout(setupName.getText()));
        JButton load = new JButton("Load");
        load.addActionListener((e) -> loadLayout(setupName.getText()));

        saveLoadActionPanel.add(save);
        saveLoadActionPanel.add(load);
        JPanel namePanel = new JPanel();

        namePanel.setLayout(new GridLayout(1,2));
        namePanel.add(setupName);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener((e) -> deleteLayout(setupName.getText()));
        namePanel.add(deleteButton);

        main.add(saveLoadActionPanel, BorderLayout.NORTH);
        main.add(namePanel, BorderLayout.SOUTH);

        loader = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getAbsolutePath() + "/KTA");
        loader.mkdir();
        File[] fileList = loader.listFiles();
        JPanel list = new JPanel();
        if (fileList != null){
            list.setLayout(new GridLayout(fileList.length, 1));
            list.setBorder(new BevelBorder(BevelBorder.RAISED));
            for (File file:fileList) {
                JPanel container = new JPanel();
                container.setBackground(Color.GRAY);
                JLabel name = new JLabel(file.getName());
                name.setHorizontalAlignment(JLabel.CENTER);
                container.setSize(list.getWidth(),(list.getHeight()/fileList.length));
                container.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        System.out.println("TRIGGER " + file.getName().split(".txt")[0]);
                        loadLayout(file.getName().split(".txt")[0]);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
                container.add(name);
                list.add(container);
            }
        } else{
            list.add(new JLabel("NO FILES!"));
        }
        main.add(list, BorderLayout.CENTER);
        main.setVisible(true);
    }
    public static void saveLayout(String name) {
        blocksStashLoad = new Vector<>();
        loader = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getAbsolutePath() + "/KTA");
        loader.mkdir();
        if (name.equals("")){name = sdfF.format(LocalDateTime.now());}
        loader = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getAbsolutePath() + "/KTA/" + name +".txt");
        try {
            FileWriter writer = new FileWriter(loader);
            for (BlocksHandler block: blocksStashLoad) {
                writer.write(block.type + " " + block.prompt + " " + block.length + "\r\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void loadLayout(String name) {
        loader = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getAbsolutePath() + "/KTA");
        loader.mkdir();
        loader = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getAbsolutePath() + "/KTA/" + name +".txt");
        try {
            Scanner FReader = new Scanner(loader);
            while (FReader.hasNextLine()) {
                String block = FReader.nextLine();
                String[] commands = block.split(" ");
                System.out.println(block);
                BlocksHandler.addPanel(new BlocksHandler(commands[0], commands[1], Integer.parseInt(commands[2])));
            }
            FReader.close();
            main.dispose();
    } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void deleteLayout(String name) {
        loader = new File(FileSystemView.getFileSystemView().getDefaultDirectory().getAbsolutePath() + "/KTA/" + name +".txt");
        boolean isDeleted = loader.delete();
        if (isDeleted){
            main.dispose();
        } else {
            System.out.println(loader.getAbsolutePath() + " " + "not deleted, file exists?:"+ loader.exists());
        }


    }
}
