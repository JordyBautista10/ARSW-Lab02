package snakepackage;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import enums.GridSize;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

/**
 * @author jd-
 *
 */
public class SnakeApp {
    private JButton botonPausa;
    private JButton botonReanudar;
    private static SnakeApp app;
    public static final int MAX_THREADS = 8;
    Snake[] snakes = new Snake[MAX_THREADS];
    private static final Cell[] spawn = {
        new Cell(1, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2,
        3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, GridSize.GRID_HEIGHT - 2),
        new Cell(1, 3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2,
        GridSize.GRID_HEIGHT - 2)};
    private JFrame frame;
    private static Board board;
    int nr_selected = 0;
    Thread[] thread = new Thread[MAX_THREADS];

    public SnakeApp() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("The Snake Race");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(618, 640);
        frame.setSize(GridSize.GRID_WIDTH * GridSize.WIDTH_BOX + 17,
                GridSize.GRID_HEIGHT * GridSize.HEIGH_BOX + 40);
        frame.setLocation(dimension.width / 2 - frame.getWidth() / 2,
                dimension.height / 2 - frame.getHeight() / 2);
        board = new Board();
        frame.add(board,BorderLayout.CENTER);
        JPanel actionsBPabel=new JPanel();
        actionsBPabel.setLayout(new FlowLayout());
        actionsBPabel.add(new JButton("Action "));
        frame.add(actionsBPabel,BorderLayout.SOUTH);
        JButton iniciar = new JButton("Iniciar");
        iniciar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                for (int i = 0; i != MAX_THREADS; i++) {
                    if(!thread[i].isAlive()) {
                        thread[i].start();
                    }
                }
            }
        });
        JButton pausar = new JButton("Pausar");
        pausar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                    int maxTamano = 0;
                    int idMaxtamano = 0;
                    int idPrimeraMuerte = -1;
                    boolean primeraMuerteEncontrada = false;

                    for (int i = 0; i < snakes.length; i++) {
                        int tamanoActual = snakes[i].getSizeSerpiente();
                        if (tamanoActual > maxTamano) {
                            maxTamano = tamanoActual;
                            idMaxtamano = snakes[i].getIdt();
                        }

                        if (snakes[i].isSnakeEnd() && !primeraMuerteEncontrada) {
                            idPrimeraMuerte = snakes[i].getIdt();
                            primeraMuerteEncontrada = true;
                        }

                        snakes[i].setJuegoCorriendo(false);
                    }

                    String mensaje = "La serpiente más larga es: " + idMaxtamano +
                            "\nLa primera serpiente muerta: " + idPrimeraMuerte;
                    JOptionPane.showMessageDialog(null, mensaje);
            }
        });

        JButton reaunudar = new JButton("Reaunudar");
        reaunudar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {

                    for (int i =0; i < snakes.length ; i++) {
                        snakes[i].resumen();
                    }
                }

        });

        actionsBPabel.add(iniciar);
        actionsBPabel.add(pausar);
        actionsBPabel.add(reaunudar);

    }

    public static void main(String[] args) {
        app = new SnakeApp();
        app.init();
    }

    private void init() {
        
        
        
        for (int i = 0; i != MAX_THREADS; i++) {
            
            snakes[i] = new Snake(i + 1, spawn[i], i + 1);
            snakes[i].addObserver(board);
            thread[i] = new Thread(snakes[i]);
            //thread[i].start();
        }

        frame.setVisible(true);

            
        while (true) {
            int x = 0;
            for (int i = 0; i != MAX_THREADS; i++) {
                if (snakes[i].isSnakeEnd() == true) {
                    x++;
                }
            }
            if (x == MAX_THREADS) {
                break;
            }
        }


        System.out.println("Thread (snake) status:");
        for (int i = 0; i != MAX_THREADS; i++) {
            System.out.println("["+i+"] :"+thread[i].getState());
        }
        

    }

    public static SnakeApp getApp() {
        return app;
    }

}
