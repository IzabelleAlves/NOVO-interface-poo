package crud.clinica.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import crud.clinica.dao.PacienteDAO;
import crud.clinica.exception.CPFJaExisteException;
import crud.clinica.model.Paciente;
import crud.clinica.view.paciente.PacienteFormDialog;
import crud.clinica.view.util.DialogManager;
import crud.clinica.view.util.ValidacaoUtil;

/**
 * Controller responsável pela lógica de negócio da tela de formulário de Paciente.
 * Ele intermedia a comunicação entre a View (PacienteFormDialog) e o Model/DAO.
 */
public class PacienteFormController {

    private final PacienteFormDialog view;
    private final PacienteDAO pacienteDAO;
    private Paciente paciente; // O paciente que está sendo criado ou editado

    public PacienteFormController(PacienteFormDialog view, PacienteDAO pacienteDAO, Paciente paciente) {
        this.view = view;
        this.pacienteDAO = pacienteDAO;
        this.paciente = paciente;
    }

    /**
     * Orquestra o processo de salvar (criar ou atualizar) um paciente.
     * Pega os dados da view, valida, chama o DAO e dá feedback ao usuário.
     */
    public void salvar() {
        // 1. Obter dados da View
        String nome = view.getNome();
        String cpf = view.getCpf().replaceAll("[^0-9]", ""); // Pega só os números
        String dataNascimentoStr = view.getDataNascimento();

        // 2. Validar os dados usando as classes utilitárias
        if (!ValidacaoUtil.validarNome(nome)) {
            DialogManager.showError(view, "Nome inválido. Deve ter entre 3 e 100 caracteres.");
            return;
        }
        if (!ValidacaoUtil.validarCPF(cpf)) {
            DialogManager.showError(view, "CPF inválido. Verifique o número digitado.");
            return;
        }
        if (!ValidacaoUtil.validarData(dataNascimentoStr)) {
            DialogManager.showError(view, "Data de Nascimento inválida. Use o formato DD/MM/AAAA.");
            return;
        }

        // 3. Converter e preparar o objeto Model
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime nascimento = LocalDate.parse(dataNascimentoStr, formatter).atStartOfDay();

            // 4. Decidir entre criar um novo ou atualizar um existente
            if (paciente == null) {
                // Modo de Criação
                paciente = new Paciente(nome, view.getCpf(), nascimento); // Salva com máscara
                pacienteDAO.create(paciente);
                DialogManager.showSuccess(view, "Paciente cadastrado com sucesso!");
            } else {
                // Modo de Edição
                paciente.setNome(nome);
                paciente.setCpf(view.getCpf()); // Salva com máscara
                paciente.setDataNascimento(nascimento);
                pacienteDAO.update(paciente);
                DialogManager.showSuccess(view, "Paciente atualizado com sucesso!");
            }

            // 5. Fechar a janela em caso de sucesso
            view.dispose();

        } catch (DateTimeParseException ex) {
            DialogManager.showError(view, "Erro no formato da data. Certifique-se que é DD/MM/AAAA.");
        } catch (CPFJaExisteException ex) {
            DialogManager.showError(view, ex.getMessage());
        } catch (Exception ex) {
            DialogManager.showError(view, "Ocorreu um erro inesperado ao salvar o paciente.\nDetalhes: " + ex.getMessage());
            ex.printStackTrace(); // Logar o erro no console para debug
        }
    }
}