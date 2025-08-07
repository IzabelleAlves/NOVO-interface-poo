package crud.clinica.controller;

import crud.clinica.exception.CPFJaExisteException;
import crud.clinica.facade.ClinicaFacade;
import crud.clinica.model.Paciente;
import crud.clinica.view.paciente.PacienteFormDialog;
import crud.clinica.view.util.DialogManager;
import crud.clinica.view.util.ValidacaoUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Controller responsável pela lógica de negócio da tela de formulário de Paciente.
 */
public class PacienteFormController {

    private final PacienteFormDialog view;
    private final ClinicaFacade facade;
    private Paciente paciente; 

    public PacienteFormController(PacienteFormDialog view, ClinicaFacade facade, Paciente paciente) {
        this.view = view;
        this.facade = facade;
        this.paciente = paciente; // Será null se for um novo cadastro, ou um objeto se for edição
    }

    /**
     * Orquestra o processo de salvar um paciente.
     */
    public void salvar() {
        String nome = view.getNome();
        String cpf = view.getCpf(); 
        String dataNascimentoStr = view.getDataNascimento();

        // 1. Validações de formato dos campos
        if (!ValidacaoUtil.validarNome(nome)) {
            DialogManager.showError(view, "Nome inválido. Deve ter entre 3 e 100 caracteres.");
            return;
        }
        if (!ValidacaoUtil.validarCPF(cpf)) {
            DialogManager.showError(view, "CPF inválido. Verifique o formato e os dígitos.");
            return;
        }
        if (dataNascimentoStr.contains("_") || !ValidacaoUtil.validarData(dataNascimentoStr)) {
            DialogManager.showError(view, "Data de Nascimento inválida ou incompleta.");
            return;
        }

        try {
            // 2. Preparação do objeto Model
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime nascimento = LocalDate.parse(dataNascimentoStr, formatter).atStartOfDay();

            // PONTO DA CORREÇÃO: Lógica de criação/edição explícita
            // Usamos uma variável local 'pacienteParaSalvar' para garantir
            // que um objeto válido seja sempre passado para a facade.
            Paciente pacienteParaSalvar;

            if (this.paciente == null) { // Modo Criação: cria uma NOVA instância
                pacienteParaSalvar = new Paciente(nome, cpf, nascimento);
            } else { // Modo Edição: atualiza a instância que já existe
                this.paciente.setNome(nome);
                this.paciente.setCpf(cpf);
                this.paciente.setDataNascimento(nascimento);
                pacienteParaSalvar = this.paciente;
            }

            // 3. Chamada à Facade com um objeto garantidamente não-nulo
            facade.salvarPaciente(pacienteParaSalvar); 
            
            DialogManager.showSuccess(view, "Paciente salvo com sucesso!");
            view.dispose();

        } catch (CPFJaExisteException ex) {
            DialogManager.showError(view, ex.getMessage());
        } catch (DateTimeParseException e) {
            DialogManager.showError(view, "Erro no formato da data. Certifique-se que é DD/MM/AAAA.");
        } catch (Exception e) {
            DialogManager.showError(view, "Ocorreu um erro inesperado ao salvar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}