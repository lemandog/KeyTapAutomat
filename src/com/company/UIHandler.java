package com.company;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import static com.company.Main.keyMasher;
import static com.company.Main.version;

public class UIHandler {
    public static Vector<BlocksHandler> blocksStash = new Vector<>(0);
    boolean listening = false;
    boolean repeat = false;
    JFrame main = new JFrame("KeyTapAutomat v" + version);
    public static JPanel blocks;
    public void init() {
        main.setSize(400,400);
        main.setLayout(new GridLayout(2,1));
        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel upperControlPanel = new JPanel(new GridLayout(1,2));
        JPanel buttonControl = new JPanel(new GridLayout(4,1));
        buttonControl.setMaximumSize(new Dimension(400,100));
        Button saverButton = new Button("Load/Save sequence");
        saverButton.addActionListener(e -> MemHandler.getSaveLoadUI());
        buttonControl.add(saverButton);

        Button repeatButton = new Button("Repeat sequence");
        repeatButton.setBackground(Color.white);
        repeatButton.addActionListener(e -> {
            repeat =! repeat;
            if (repeat){
                repeatButton.setBackground(Color.GREEN);
            } else{
                repeatButton.setBackground(Color.white);}
        });
        buttonControl.add(repeatButton);

        Button resetButton = new Button("Reset sequence");
        resetButton.addActionListener(e -> resetLayout());
        buttonControl.add(resetButton);

        Button startButton = new Button("Start sequence");
        startButton.addActionListener(e -> startSequence());
        buttonControl.add(startButton);
        upperControlPanel.add(buttonControl);

        JPanel pauseControl = new JPanel(new GridLayout(3,1));
        Button addPauseBlockButton = new Button("Add pause block");
        Button addFunctionBlockButton = new Button("Add button block");
        pauseControl.setSize(400,200);
        JSlider pauseLength = new JSlider();
        pauseLength.setMaximum(10000);
        pauseLength.setMinimum(0);
        pauseLength.setValue(10);
        pauseLength.setMajorTickSpacing(500);
        pauseLength.setMinorTickSpacing(50);
        pauseLength.setSnapToTicks(true);
        pauseLength.setPaintTicks(true);
        JLabel pauseLengthText = new JLabel("10 ms");
        pauseLength.addChangeListener(e -> {
            pauseLengthText.setText(pauseLength.getValue() + " ms");
        });
        addPauseBlockButton.addActionListener(e -> BlocksHandler.addPause(pauseLength.getValue()));
        addFunctionBlockButton.setBackground(Color.white);
        addFunctionBlockButton.addActionListener( e -> {
            listening = !listening;
            if (listening){
                addFunctionBlockButton.setBackground(Color.red);
                pauseControl.grabFocus();
            } else{addFunctionBlockButton.setBackground(Color.white);}

            });
        pauseControl.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (listening){
                    BlocksHandler.addPrompt(String.valueOf(e.getKeyCode()), "Keyboard");
                }
                blocks.revalidate();
                blocks.repaint();
            }
        });
        pauseControl.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (listening){
                    BlocksHandler.addPrompt(String.valueOf(e.getButton()), "Mouse");
                }
                blocks.revalidate();
                blocks.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        pauseControl.add(pauseLengthText);
        pauseControl.add(pauseLength);
        pauseControl.add(addFunctionBlockButton);
        pauseControl.add(addPauseBlockButton);
        upperControlPanel.add(pauseControl);
        main.add(upperControlPanel);

        blocks = new JPanel();
        blocks.setMinimumSize(new Dimension(400,200));
        blocks.setBackground(Color.black);
        main.add(blocks);
        main.setVisible(true);
    }
    private void startSequence() {
        try {
            keyMasher = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
                do {
                    for (BlocksHandler block : blocksStash) {
                        Color was = block.block.getBackground();
                        block.block.setBackground(Color.red);
                        blocks.revalidate();
                        blocks.repaint();
                        System.out.println(block.prompt + " " + block.length + " " + block.type);
                        switch (block.type) {
                            case "pause" -> {
                                try {
                                    Thread.sleep(block.length);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            case "promptKeyboard" -> {
                                keyMasher.keyPress(Integer.parseInt(block.prompt));
                                keyMasher.delay(10);
                                keyMasher.keyRelease(Integer.parseInt(block.prompt));
                            }
                            case "promptMouse" -> {
                                int mask = 0;
                                switch (Integer.parseInt(block.prompt)){
                                    case 1: {
                                        mask = InputEvent.BUTTON1_MASK;
                                        break;
                                    }
                                    case 2: {
                                        mask = InputEvent.BUTTON2_MASK;
                                        break;
                                    }
                                    case 3: {
                                        mask = InputEvent.BUTTON3_MASK;
                                        break;
                                    }
                                }
                                keyMasher.mousePress(mask);
                                keyMasher.delay(10);
                                keyMasher.mouseRelease(mask);
                            }
                        }
                        block.block.setBackground(was);
                    }
                } while (repeat);
            }).start();
    }
    private void resetLayout(){
        for (BlocksHandler block: blocksStash) {
            blocks.remove(block.block);
        }
        blocksStash.clear();
        UIHandler.blocks.revalidate();
        UIHandler.blocks.repaint();
    }
}
