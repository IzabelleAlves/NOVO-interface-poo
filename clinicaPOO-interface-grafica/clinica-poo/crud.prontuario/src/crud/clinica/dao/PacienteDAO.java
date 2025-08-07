package crud.clinica.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import crud.clinica.database.IConnection;
import crud.clinica.exception.CPFJaExisteException;
import crud.clinica.facade.ClinicaFacade;
import crud.clinica.model.Paciente;

// A interface IEntityDAO é ótima!
public class PacienteDAO implements IEntityDAO<Paciente> {

    private final IConnection conn;

    // O construtor agora só precisa da conexão. Simples e focado.
    public PacienteDAO(IConnection dbConnection) {
        this.conn = (IConnection) dbConnection;
    }

    @Override
    public void create(Paciente t) throws SQLException, CPFJaExisteException {
        // A verificação de CPF duplicado está correta aqui.
        if (existsByCPF(t.getCpf())) {
            throw new CPFJaExisteException("Já existe um paciente com o CPF informado.");
        }

        String sql = "INSERT INTO PACIENTES (nome, cpf, data_nascimento) VALUES (?, ?, ?);";
        try (PreparedStatement pstm = conn.getConnection().prepareStatement(sql)) {
            pstm.setString(1, t.getNome());
            pstm.setString(2, t.getCpf());
            pstm.setTimestamp(3, Timestamp.valueOf(t.getDataNascimento()));
            pstm.execute();
        } catch (SQLException e) {
            // Lançar a exceção para que a camada superior (Facade/Controller) possa tratá-la.
            throw new SQLException("Erro ao criar paciente.", e);
        }
    }

    @Override
    public Paciente findById(Long id) throws SQLException {
        Paciente p = null;
        String sql = "SELECT * FROM PACIENTES WHERE ID = ?;";
        try (PreparedStatement pstm = conn.getConnection().prepareStatement(sql)) {
            pstm.setLong(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    p = new Paciente(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getTimestamp("data_nascimento").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao buscar paciente por ID.", e);
        }
        return p;
    }

    @Override
    public List<Paciente> findAll() throws SQLException {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM PACIENTES ORDER BY nome;";
        try (PreparedStatement pstm = conn.getConnection().prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                pacientes.add(new Paciente(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getTimestamp("data_nascimento").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao listar todos os pacientes.", e);
        }
        return pacientes;
    }

    @Override
    public void update(Paciente t) throws SQLException, CPFJaExisteException {
        // Lógica para verificar se o novo CPF já pertence a OUTRO paciente.
        Paciente existente = findByExactCPF(t.getCpf());
        if (existente != null && !existente.getId().equals(t.getId())) {
            throw new CPFJaExisteException("Já existe outro paciente com o CPF informado.");
        }
        
        String sql = "UPDATE PACIENTES SET NOME = ?, CPF = ?, data_nascimento = ? WHERE ID = ?;";
        try (PreparedStatement pstm = conn.getConnection().prepareStatement(sql)) {
            pstm.setString(1, t.getNome());
            pstm.setString(2, t.getCpf());
            pstm.setTimestamp(3, Timestamp.valueOf(t.getDataNascimento()));
            pstm.setLong(4, t.getId());
            pstm.execute();
        } catch (SQLException e) {
            throw new SQLException("Erro ao atualizar paciente.", e);
        }
    }
    
    // O delete no seu DDL já usa ON DELETE CASCADE, o que é ótimo!
    // O banco de dados já se encarrega de remover os exames.
    // O método aqui só precisa deletar o paciente.
    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM PACIENTES WHERE ID = ?;";
        try (PreparedStatement pstm = conn.getConnection().prepareStatement(sql)) {
            pstm.setLong(1, id);
            pstm.execute();
        } catch (SQLException e) {
            throw new SQLException("Erro ao deletar paciente.", e);
        }
    }
    
    // Método auxiliar para busca por CPF exato
    public Paciente findByExactCPF(String cpf) throws SQLException {
        // Implementação similar ao findById, mas com WHERE cpf = ?
        // Retorna um único Paciente ou null.
        // ...
        return null; // Implementar
    }
    
    // Método auxiliar para verificar existência de CPF
    public boolean existsByCPF(String cpf) throws SQLException {
        // Implementação que faz um "SELECT 1 FROM pacientes WHERE cpf = ?"
        // Retorna true se encontrar, false se não.
        // ...
        return false; // Implementar
    }
    
    // DELETE por ID em vez de objeto
    @Override
    public void delete(Paciente t) throws Exception {
       delete(t.getId());
    }

}