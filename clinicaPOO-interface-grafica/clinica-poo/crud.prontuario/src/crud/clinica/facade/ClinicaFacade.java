package crud.clinica.facade;

import java.util.List;
import crud.clinica.dao.ExameDAO;
import crud.clinica.dao.PacienteDAO;
import crud.clinica.database.IConnection;
import crud.clinica.exception.CPFJaExisteException;
import crud.clinica.model.Exame;
import crud.clinica.model.Paciente;

/**
 * Fachada (Facade) que simplifica o acesso aos subsistemas de dados (DAOs).
 * É o único ponto de contato para a camada de controle (Controllers).
 */
public class ClinicaFacade {

    private final PacienteDAO pacienteDAO;
    private final ExameDAO exameDAO;

    public ClinicaFacade(IConnection dbConnection) {
        this.pacienteDAO = new PacienteDAO(dbConnection);
        this.exameDAO = new ExameDAO(dbConnection);
    }

    // --- MÉTODOS DE ORQUESTRAÇÃO PARA PACIENTE ---

    public void salvarPaciente(Paciente paciente) throws Exception {
        if (paciente.getId() == null || paciente.getId() == 0) {
            pacienteDAO.create(paciente);
        } else {
            pacienteDAO.update(paciente);
        }
    }
    
    // A deleção é simples pois o banco já faz o CASCADE.
    public void deletarPaciente(long pacienteId) throws Exception {
        pacienteDAO.delete(pacienteId);
    }
    
    /**
     * Lista todos os pacientes e, para cada um, preenche a contagem de exames.
     * Esta é a orquestração que o PacienteDAO não faz mais sozinho.
     */
    public List<Paciente> listarPacientesComContagemDeExames() throws Exception {
        List<Paciente> pacientes = pacienteDAO.findAll();
        for (Paciente p : pacientes) {
            int qtdExames = exameDAO.countByPacienteId(p.getId());
            p.setQuantidadeExames(qtdExames);
        }
        return pacientes;
    }

    public Paciente buscarPacientePorId(long id) throws Exception {
        return pacienteDAO.findById(id);
    }

    // NOVO MÉTODO - Adicione este método à sua classe ClinicaFacade
    /**
     * Lista todos os pacientes de forma simples, sem contagem de exames.
     * Ideal para preencher ComboBoxes e outras listas rápidas.
     * @return Uma lista de objetos Paciente.
     * @throws Exception se ocorrer um erro na consulta.
     */
    public List<Paciente> listarTodosPacientes() throws Exception {
        return pacienteDAO.findAll();
    }
    // --- MÉTODOS DE ORQUESTRAÇÃO PARA EXAME ---

    public void salvarExame(Exame exame) throws Exception {
         if (exame.getId() == null || exame.getId() == 0) {
            exameDAO.create(exame);
        } else {
            exameDAO.update(exame);
        }
    }

    public List<Exame> listarTodosExames() throws Exception {
        return exameDAO.findAll();
    }
    
    /**
     * Delega a busca de exames por nome de paciente para o ExameDAO.
     */
    public List<Exame> buscarExamesPorNomePaciente(String nome) throws Exception {
        return exameDAO.findByNomePaciente(nome);
    }
    
    public void deletarExame(long exameId) throws Exception {
        // Simplesmente delega a chamada.
        Exame ex = new Exame();
        ex.setId(exameId);
        exameDAO.delete(ex);
    }
    
    
}