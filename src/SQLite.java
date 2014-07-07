import java.sql.*;
import java.util.ArrayList;

/**
 * Класс SQLite предназначен для работы с базами данных JDBC. С помощью него
 * можно создавать, заполнять и считывать данные из баз.
 * 
 * @author Игорь Трофименко и Волощук Дмитрий
 *
 */
public class SQLite {
	/**
	 * Создает подключение с базой по именем DBname.
	 * 
	 * @param DBname
	 *            - имя базы данных
	 * @return connection
	 */
	private Connection GetConnection(String DBname) {
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + DBname + ".db");
			c.setAutoCommit(false);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return c;
	}

	/**
	 * Метод создания и заполнения базы с вопросами. Создает базу и таблицу в
	 * ней с полями: id - номер вопроса, Question - текст вопроса, Options -
	 * возможные варианты ответа, Correct - правильный ответ на вопрос.
	 */
	public void FillBase() {
		Connection c = GetConnection("test1");
		Statement stmt = null;
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "CREATE TABLE TEST "
					+ "(Id INT PRIMARY KEY     NOT NULL,"
					+ " Question        TEXT    NOT NULL, "
					+ " Options        	TEXT 	NOT NULL,"
					+ " Correct         TEXT	NOT NULL)";
			stmt.executeUpdate(sql);
			String insertText = "INSERT INTO TEST (Id,Question,Options,Correct)";
			sql = insertText
					+ "VALUES (0, 'В каком году началась первая Мировая Война?','a) 1905\n b) 1914\n c) 1918\n d) 1922', '1914' );";
			stmt.executeUpdate(sql);
			sql = insertText
					+ "VALUES (1, 'В какой союз входила Российская Империя во время первой Мировой Войны?','a) Тройственный\nb) Антанта\nc) Движение\nd) Вперед', 'Антанта' );";
			stmt.executeUpdate(sql);
			sql = insertText
					+ "VALUES (2, 'В каком году Крым был присоединен к територии УССР?','a) 1945\nb) 1956\nc) 1954\nd) 1955','1954');";
			stmt.executeUpdate(sql);
			sql = insertText
					+ "VALUES (3, 'Сколько лет Крым был частью Украины?','a) 120\nb) 60\nc) 54\nd) 23','60');";
			stmt.executeUpdate(sql);
			sql = insertText
					+ "VALUES (4, 'Кто был президентом США во время Второй Мировой Войны?','a) Кеннеди\nb) Джорж Буш\nc) Рузвельт\nd) Линкольн', 'Рузвельт' );";
			stmt.executeUpdate(sql);
			sql = insertText
					+ "VALUES (5, 'В каком году был открыт второй фронт во время Второй Мировой Войны?','a) 1942\nb) 1941\nc) 1946\nd) 1944','1944');";
			stmt.executeUpdate(sql);
			sql = insertText
					+ "VALUES (6, 'Кто отец водородной бомбы?','a) Денисов\nb) Сахаров\nc) Демидович\nd) Каралев', 'Сахаров');";
			stmt.executeUpdate(sql);
			sql = insertText
					+ "VALUES (7, 'В каком году закончилась Вторая Мировоя Война?','a) 1944\nb) 1945\nc) 1946\nd) 1943', '1945');";
			stmt.executeUpdate(sql);
			sql = insertText
					+ "VALUES (8, 'Кто был первым и последним президентом СССР?','a) Хрущев\nb) Андропов\nc) Сталин\nd) Горбачев', 'Горбачев' );";
			stmt.executeUpdate(sql);
			sql = insertText
					+ "VALUES (9, 'Кто был лидером ОУН(Б)?','a) Виниченко\nb) Петлюра\nc) Бандера\nd) Грушевский', 'Бандера');";
			stmt.executeUpdate(sql);
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Records created successfully");
	}

	/**
	 * Метод чтения из базы данных с вопросами. Читает данные из таблицы и
	 * заполняет списки с вопросами, вариантами ответа и правильными ответами
	 * 
	 * @param listQuestions
	 *            - список вопросов
	 * @param listAnswers
	 *            - список вариантов ответа
	 * @param listTrueAnswers
	 *            - список правильных ответов
	 */
	public void read(ArrayList<String> listQuestions,
			ArrayList<String> listAnswers, ArrayList<String> listTrueAnswers) {

		Connection c = GetConnection("test1");
		Statement stmt = null;
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM TEST;");
			while (rs.next()) {
				String question = rs.getString("Question");
				String options = rs.getString("Options");
				String correctOpt = rs.getString("Correct");
				listQuestions.add(question);
				listAnswers.add(options);
				listTrueAnswers.add(correctOpt);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

	}

	/**
	 * Создает пустую базу данных с информации о проведенных тестах Создает
	 * таблицу TESTS с полями: Name - имя студента, Answers - информация об
	 * ответах на вопросы(Корректность ответов, время выполнения), Mark - оценка
	 * за тест
	 * 
	 * @param name
	 *            - имя бд
	 */
	public void createTestBase(String name) {
		Connection c = GetConnection(name);
		Statement stmt = null;
		try {
			c.setAutoCommit(false);
			System.out.println("Opened database " + name + " successfully");
			stmt = c.createStatement();
			String sql = "CREATE TABLE TESTS " + "(Name TEXT NOT NULL,"
					+ " Answers TEXT NOT NULL," + " Mark INT NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Table created successfully");
	}

	/**
	 * Записывает данные о прошедшем тесте в базу с результатами
	 * 
	 * @param DBname
	 *            - имя базы результатов
	 * @param StudentName
	 *            - имя студента
	 * @param result
	 *            - результат прохождения теста
	 * @param mark
	 *            - оценка
	 */
	public void write(String DBname, String StudentName,
			ArrayList<String> result, int mark) {
		Connection c = GetConnection(DBname);
		Statement stmt = null;
		try {
			c.setAutoCommit(false);
			System.out.println("Opened for write database " + DBname
					+ " successfully");
			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS TESTS "
					+ "(Name TEXT NOT NULL," + " Answers TEXT NOT NULL,"
					+ " Mark INT NOT NULL)";
			stmt.executeUpdate(sql);
			String answers = "";
			for (String str : result) {
				answers += str;
			}
			sql = "INSERT INTO TESTS (Name, Answers, Mark)" + "VALUES ('"
					+ StudentName + "', '" + answers + "', " + mark + ");";
			stmt.executeUpdate(sql);
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Records created successfully");
	}

	/**
	 * Считывает результаты тестов из базы данных
	 * 
	 * @param DBname
	 *            - имя базы данных
	 * @param StudentName
	 *            - имя студента
	 * @return string - информация о проведенном тесте
	 */
	public String ReadResult(String DBname, String StudentName) {
		Connection c = GetConnection(DBname);
		Statement stmt = null;
		String result = "";
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM TESTS;");
			while (rs.next()) {
				String name = rs.getString("Name");
				if (name.equals(StudentName)) {
					String answers = rs.getString("Answers");
					int mark = rs.getInt("Mark");
					result = (StudentName + "\n" + answers + "\n" + "Оценка: " + mark);
				}

			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return result;
	}
}
