package crud.clinica.controller;

import crud.clinica.facade.ClinicaFacade;
import crud.clinica.model.Exame;
import crud.clinica.model.Paciente;
import crud.clinica.view.exame.ExameFormDialog;
import crud.clinica.view.util.DialogManager;
import crud.clinica.view.util.ValidacaoUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ExameFormController {

    private final ExameFormDialog view;
    private final ClinicaFacade facade;
    private Exame exame; 

    public ExameFormController(ExameFormDialog view, ClinicaFacade facade, Exame exame) {
        this.view = view;
        this.facade = facade;
        this.exame = exame;
    }

    public void inicializar() {
        try {
            List<Paciente> pacientes = facade.listarTodosPacientes();
            view.setPacientesNoComboBox(pacientes);
        } catch (Exception e) {
            DialogManager.showError(view, "Erro ao carregar a lista de pacientes: " + e.getMessage());
        }
    }

    public void salvar() {
        Paciente pacienteSelecionado = view.getPacienteSelecionado();
        String dataStr = view.getData();
        String descricao = view.getDescricao();

        if (pacienteSelecionado == null) {
            DialogManager.showError(view, "Selecione um paciente.");
            return;
        }
        if (descricao.trim().isEmpty()) {
            DialogManager.showError(view, "O campo 'Descrição' é obrigatório.");
            return;
        }
        if (!ValidacaoUtil.validarData(dataStr)) {
            DialogManager.showError(view, "A data informada é inválida. Use o formato DD/MM/AAAA.");
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataExame = LocalDate.parse(dataStr, formatter);

            if (exame == null) { 
                exame = new Exame(descricao, dataExame);
                exame.setPaciente(pacienteSelecionado);
            } else { 
                exame.setDescricao(descricao);
                exame.setData_exame(dataExame);
                exame.setPaciente(pacienteSelecionado);
            }

            facade.salvarExame(exame);
            DialogManager.showSuccess(view, "Exame salvo com sucesso!");
            view.dispose();

        } catch (DateTimeParseException e) {
            DialogManager.showError(view, "Erro no formato da data. Certifique-se que é DD/MM/AAAA.");
        } catch (Exception e) {
            DialogManager.showError(view, "Ocorreu um erro ao salvar o exame: " + e.getMessage());
            e.printStackTrace();
        }
    }
}