package crud.clinica.view.util;

import javax.swing.JOptionPane;
import java.awt.Component;

/**
 * Classe utilitária final para gerenciar a exibição de caixas de diálogo (JOptionPane).
 * Centraliza as mensagens de sucesso, erro e confirmação.
 */
public final class DialogManager {

    /**
     * Construtor privado para impedir a instanciação.
     */
    private DialogManager() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    /**
     * Exibe uma mensagem de erro padrão.
     * @param parent O componente pai sobre o qual o diálogo será centralizado.
     * @param message A mensagem de erro a ser exibida.
     */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Exibe uma mensagem de sucesso padrão.
     * @param parent O componente pai sobre o qual o diálogo será centralizado.
     * @param message A mensagem de sucesso a ser exibida.
     */
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Exibe uma caixa de diálogo de confirmação (Sim/Não).
     * @param parent O componente pai sobre o qual o diálogo será centralizado.
     * @param message A pergunta de confirmação a ser exibida.
     * @return {@code true} se o usuário clicar em "Sim", {@code false} caso contrário.
     */
    public static boolean showConfirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
}