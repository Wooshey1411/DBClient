package ru.nsu.database.airportclient.model.tables.describing;

import ru.nsu.database.airportclient.model.tables.ITable;
import ru.nsu.database.airportclient.model.tables.utils.LinkedToken;
import ru.nsu.database.airportclient.model.tables.utils.Token;
import ru.nsu.database.airportclient.model.tables.utils.TokenType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Airplanes implements ITable {

    public final static String TABLE_NAME = "airplanes";

    public final static String TABLE_NAME_ALIAS = "Самолеты";
    public final static List<Token> TOKENS = Arrays.asList(
            new Token("airplane_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("airplane_model_id", false, TokenType.INTEGER, null, true, true, "ИД модели"),
            new Token("airplane_pilots_brigade", false, TokenType.INTEGER, null, true, true, "ИД бригады пилотов"),
            new Token("airplane_technican_brigade", false, TokenType.INTEGER, null, true, true, "ИД бригады техников"),
            new Token("airplane_service_brigade", false, TokenType.INTEGER, null, true, true, "ИД бригады обслуживания"),
            new Token("airplane_arrival_date", false, TokenType.DATE, null, true, true, "Дата поступления"),
            new Token("airplane_name", false, TokenType.VARCHAR, null, true, true, "Название")
    );

    public final static List<Token> DATA_TOKENS = Arrays.asList(
            new Token("airplane_id", true, TokenType.INTEGER, null, false, true, "Идентификатор"),
            new Token("airplane_model_id", false, TokenType.INTEGER, null, false, true, "ИД модели"),
            new Token("airplane_model_name", false, TokenType.VARCHAR, null, true, true, "Модель"),
            new Token("airplane_name", false, TokenType.VARCHAR, null, true, true, "Название"),
            new Token("airplane_pilots_brigade", false, TokenType.INTEGER, null, true, true, "Бригада пилотов"),
            new Token("airplane_technican_brigade", false, TokenType.INTEGER, null, true, true, "Бригада техников"),
            new Token("airplane_service_brigade", false, TokenType.INTEGER, null, true, true, "Бригада обслуживания"),
            new Token("airplane_arrival_date", false, TokenType.DATE, null, true, true, "Дата поступления")
    );

    public final static List<LinkedToken> ADD_TOKENS = Arrays.asList(
            new LinkedToken("airplane_model", "Модель", false, TokenType.INTEGER, 0, "select airplane_model_id, airplane_model_name from airplane_models", Arrays.asList(
                    new Token("airplane_model_id", true, TokenType.INTEGER, null, false, true, "ИД модели"),
                    new Token("airplane_model_name", false, TokenType.VARCHAR, null, true, true, "Модель")
            )),
            new LinkedToken("airplane_name", "Название", false, TokenType.VARCHAR, 40, null, null),
            new LinkedToken("airplane_arrival_date", "Дата поступления", false, TokenType.DATE, 0, null, null),
            new LinkedToken("airplane_pilots_brigade", "Бригада пилотов", false, TokenType.INTEGER, 0, "select brigade_id as airplane_pilots_brigade from brigades join departments d on d.department_id = brigades.brigare_department_id where department_specialization = 1", List.of(
                    new Token("airplane_pilots_brigade", true, TokenType.INTEGER, null, true, true, "ИД")
            )),
            new LinkedToken("airplane_technican_brigade", "Бригада техников", false, TokenType.INTEGER, 0, "select brigade_id as airplane_technican_brigade from brigades join departments d on d.department_id = brigades.brigare_department_id where department_specialization = 3", List.of(
                    new Token("airplane_technican_brigade", true, TokenType.INTEGER, null, true, true, "ИД")
            )),
            new LinkedToken("airplane_service_brigade", "Бригада обслуживания", false, TokenType.INTEGER, 0, "select brigade_id as airplane_service_brigade from brigades join departments d on d.department_id = brigades.brigare_department_id where department_specialization = 4", List.of(
                    new Token("airplane_service_brigade", true, TokenType.INTEGER, null, true, true, "ИД")
            ))
    );

    public final static List<LinkedToken> EDIT_TOKENS = Arrays.asList(
            new LinkedToken("airplane_name", "Название", false, TokenType.VARCHAR, 40, null, null),
            new LinkedToken("airplane_arrival_date", "Дата поступления", false, TokenType.DATE, 0, null, null),
            new LinkedToken("airplane_pilots_brigade", "Бригада пилотов", false, TokenType.INTEGER, 0, "select brigade_id as airplane_pilots_brigade from brigades join departments d on d.department_id = brigades.brigare_department_id where department_specialization = 1", List.of(
                    new Token("airplane_pilots_brigade", true, TokenType.INTEGER, null, true, true, "ИД")
            )),
            new LinkedToken("airplane_technican_brigade", "Бригада техников", false, TokenType.INTEGER, 0, "select brigade_id as airplane_technican_brigade from brigades join departments d on d.department_id = brigades.brigare_department_id where department_specialization = 3", List.of(
                    new Token("airplane_technican_brigade", true, TokenType.INTEGER, null, true, true, "ИД")
            )),
            new LinkedToken("airplane_service_brigade", "Бригада обслуживания", false, TokenType.INTEGER, 0, "select brigade_id as airplane_service_brigade from brigades join departments d on d.department_id = brigades.brigare_department_id where department_specialization = 4", List.of(
                    new Token("airplane_service_brigade", true, TokenType.INTEGER, null, true, true, "ИД")
            ))

    );


    public final static HashMap<String, String> CONSTRAINTS_TEXTS = new HashMap<>(Map.of(
            "airplanes_airplane_name_key", "Самолёт с таким названием уже есть",
            "airplanes_airplane_arrival_date_check","Самолёт не мог поступить раньше 2020-01-01"

    ));

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public List<Token> getAllTokens() {
        return TOKENS;
    }

    @Override
    public String getTableAlias() {
        return TABLE_NAME_ALIAS;
    }

    @Override
    public List<Token> getDataTokens() {
        return DATA_TOKENS;
    }

    @Override
    public String getRequestForGetData() {
        return "select airplane_id, am.airplane_model_id, airplane_model_name, airplane_name, airplane_pilots_brigade," +
                " airplane_technican_brigade, airplane_service_brigade, airplane_arrival_date" +
                " from airplanes join airplane_models am on am.airplane_model_id = airplanes.airplane_model_id";
    }

    @Override
    public List<LinkedToken> getTokensForAdd() {
        return ADD_TOKENS;
    }

    @Override
    public String getSQLForAddData() {
        return "insert into airplanes(airplane_model_id, airplane_name, airplane_arrival_date, airplane_pilots_brigade, airplane_technican_brigade, airplane_service_brigade) values(? , ? , ? , ? , ? , ?)";
    }

    @Override
    public HashMap<String, String> getConstraintTexts() {
        return CONSTRAINTS_TEXTS;
    }

    @Override
    public String getSQLForUpdate() {
        return "update airplanes set airplane_name = ? , airplane_arrival_date = ? , airplane_pilots_brigade = ? , airplane_technican_brigade = ? , airplane_service_brigade = ? where airplane_id = ?";
    }

    @Override
    public List<LinkedToken> getTokensForUpdate() {
        return EDIT_TOKENS;
    }

    @Override
    public String getSQLForDelete() {
        return null;
    }
}
