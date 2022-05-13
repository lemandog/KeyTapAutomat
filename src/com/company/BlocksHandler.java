package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static com.company.UIHandler.blocksStash;
import static java.awt.event.MouseEvent.BUTTON1;

public class BlocksHandler {
    JPanel block;
    String prompt;
    int length;
    String type;
    BlocksHandler(String type, String promptCode, int length){
        block = new JPanel();
        block.setSize(100,30);
        this.type = type;
        switch (type) {
            case "pause" -> {
                block.setBackground(Color.orange);
                this.length = length;
                block.add(new Label("PAUSE: " + length + " ms "));
            }
            case "promptKeyboard" -> {
                block.setBackground(Color.white);
                this.prompt = promptCode;
                block.add(new Label("PROMPT: " + promptCode + " " + KeyEvent.getKeyText(Integer.parseInt(promptCode)) +" KBTN"));
            }
            case "promptMouse" -> {
                block.setBackground(Color.blue);
                this.prompt = promptCode;
                block.add(new Label("PROMPT: " + promptCode + " MBTN"));
            }
        }
        block.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getButton());
                if (e.getButton() == BUTTON1) {
                    blocksStash.removeElement(this);
                    UIHandler.blocks.remove(block);
                }
                UIHandler.blocks.revalidate();
                UIHandler.blocks.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

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
    }
    public static void addPanel(BlocksHandler block){
        blocksStash.add(block);
        UIHandler.blocks.add(block.block);
        UIHandler.blocks.revalidate();
        block.block.revalidate();
        UIHandler.blocks.repaint();
        block.block.repaint();
    }
    public static void addPause(int value) {
        BlocksHandler block = new BlocksHandler("pause","p", value);
        addPanel(block);
    }

    public static void addPrompt(String prompt, String from) {
        BlocksHandler block = new BlocksHandler("prompt" + from,prompt, 0);
        addPanel(block);
    }
}
