package GUI;

import Logic.Board;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Vanji on 12/4/2015.
 */
public class BoardGUI extends JLabel implements ActionListener
{
    private int i,j;

    /**
     * Konstruktor
     * @param startX kondisi awal x
     * @param startY kondisi awal y
     * @param delay selang waktu pemrosesan
     * @param gambar string yang menunjukkan path file gambar
     * @param x indeks untuk Field di Vector of Field
     * @param y indeks untuk item di Vector of Boolean
     */
    public BoardGUI(
            int startX, int startY,
            int delay, String gambar, int x, int y)
    {
        this.i=x;
        this.j=y;
        setIcon( new ImageIcon(gambar) );
        setSize(50, 50);
        setLocation(startX, startY);
        new javax.swing.Timer(delay, this).start();
    }

    /**
     * Method actionPerformed
     * Merupakan method implementasi dari kelas ActionListener
     * @param actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent)
    {
        setIcon(new ImageIcon("Log.jpg"));
    }
}

