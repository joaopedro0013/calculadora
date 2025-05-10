import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App extends JFrame implements ActionListener {

    private JTextField display;
    private JPanel buttonPanel;
    private String[] buttonLabels = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            ".", "0", "=", "+", "Del", "√", "x^2"
    };

    private calculosBasicos calc = new calculosBasicos();
    private double operand1 = 0;
    private String operator = "";

    public App() {
        setTitle("Calculadora");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0x2a2a2a)); // Define o fundo da janela

        display = new JTextField();
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setPreferredSize(new Dimension(300, 50));
        display.setFont(new Font("Arial", Font.PLAIN, 24));
        display.setBackground(new Color(0x444444)); // Fundo do display
        display.setForeground(Color.WHITE); // Cor do texto no display
        display.setBorder(new EmptyBorder(10, 10, 10, 10)); // Adiciona padding
        add(display, BorderLayout.NORTH);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5)); // Adiciona espaçamento entre os botões
        buttonPanel.setBackground(new Color(0x2a2a2a)); // Define o fundo do painel de botões
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Adiciona padding ao redor dos botões
        add(buttonPanel, BorderLayout.CENTER);

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(this);
            button.setPreferredSize(new Dimension(60, 60));
            button.setFont(new Font("Arial", Font.PLAIN, 24));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setOpaque(true);
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(0x333333)); // Cor de fundo dos botões
            if ("+-*/=".contains(label) || "Del".equals(label) || "√".equals(label) || "x^2".equals(label)) {
                button.setBackground(new Color(0x444444)); // Cor de fundo diferenciada para operadores
            }
            if (label.equals("x^2")) {
                button.setText("x\u00B2"); // Usando o caractere Unicode para o símbolo de "elevado ao quadrado"
            }
            if (label.equals("Del")) {
                 button.setText("Del");
            }
            buttonPanel.add(button);
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if ("0123456789.".contains(command)) {
            display.setText(display.getText() + command);
        } else if ("+-*/".contains(command)) {
            if (!display.getText().isEmpty()) {
                operand1 = Double.parseDouble(display.getText());
                operator = command;
                display.setText(display.getText() + command); // Mantém o operando e o operador no display
            }
        } else if (command.equals("=")) {
            if (!display.getText().isEmpty()) {
                String currentText = display.getText();
                // Encontra a posição do operador no texto
                int operatorIndex = currentText.indexOf(operator);

                if (operatorIndex > 0) {
                    // Extrai o segundo operando do texto
                    double operand2 = Double.parseDouble(currentText.substring(operatorIndex + 1));
                    double result = 0;

                    switch (operator) {
                        case "+":
                            result = calc.soma(operand1, operand2);
                            break;
                        case "-":
                            result = calc.subtracao(operand1, operand2);
                            break;
                        case "*":
                            result = calc.multiplicacao(operand1, operand2);
                            break;
                        case "/":
                            if (operand2 != 0) {
                                result = calc.divisao(operand1, operand2);
                            } else {
                                display.setText("Erro: Divisão por zero");
                                return;
                            }
                            break;
                        case "√":
                            result = calc.raiz(operand1);
                            break;
                        case "x^2":
                            result = calc.potencia(operand1, operand2);
                            break;
                    }

                    // Formata o resultado para exibir no máximo duas casas decimais
                    if (result == (int) result) {
                        display.setText(String.valueOf((int) result)); // Converte para inteiro se não tiver decimal
                    } else {
                        display.setText(String.format("%.2f", result)); // Formata para duas casas decimais
                    }
                }
            }
        } else if (command.equals("Del")) { // Adicionado a lógica para o botão Apagar
            String currentText = display.getText();
            if (!currentText.isEmpty()) {
                display.setText(currentText.substring(0, currentText.length() - 1));
            }
        } else if (command.equals("√")) {
            if (!display.getText().isEmpty()) {
                operand1 = Double.parseDouble(display.getText());
                double result = calc.raiz(operand1);
                // Formata o resultado para exibir no máximo duas casas decimais
                if (result == (int) result) {
                    display.setText(String.valueOf((int) result)); // Converte para inteiro se não tiver decimal
                } else {
                    display.setText(String.format("%.2f", result)); // Formata para duas casas decimais
                }
            }
        } else if (command.equals("x^2")) {
            if (!display.getText().isEmpty()) {
                operand1 = Double.parseDouble(display.getText());
                double result = calc.potencia(operand1, 2);
                // Formata o resultado para exibir no máximo duas casas decimais
                if (result == (int) result) {
                    display.setText(String.valueOf((int) result)); // Converte para inteiro se não tiver decimal
                } else {
                    display.setText(String.format("%.2f", result)); // Formata para duas casas decimais
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App());
    }
}