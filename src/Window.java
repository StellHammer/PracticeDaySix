import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Класс Window создает графический интерфейс с помощью технологий AWT и Swing
 * 
 * @author Игорь Трофименко и Волощук Дмитрий
 */
public class Window {
	private JFrame frame;
	public Button answer1;
	private Button answer2;
	private Button answer3;
	private Button answer4;
	private Button ok;
	private Button start;
	private TextField tf;
	private TextField fname;
	private TextField lname;
	private TextField group;
	private TextArea result;
	private Label lbFName;
	private Label lbLName;
	private Label lbGroup;
	private int i = 0;
	long before;
	long after;
	String DBname = "FirstTest";
	StringBuilder sb = new StringBuilder();

	SQLite sq;
	ArrayList<String> listQuestions;
	ArrayList<String> listAnswers;
	ArrayList<String> listTrueAnswers;
	ArrayList<String> listTrueAnswersStudet;
	ArrayList<String> resultList;
	ArrayList<Integer> random;
	Work work = new Work();

	/**
	 * Конструктор. Создает все графические элементы. Создает объекты классов.
	 */
	public Window() {
		// окно
		frame = new JFrame();
		// кнопки
		answer1 = new Button();
		answer2 = new Button();
		answer3 = new Button();
		answer4 = new Button();
		start = new Button("Старт");
		ok = new Button("ok");
		// текстовое поле
		tf = new TextField();
		fname = new TextField();
		lname = new TextField();
		group = new TextField();
		result = new TextArea();
		// Label
		lbFName = new Label("Имя:");
		lbLName = new Label("Фамилия:");
		lbGroup = new Label("Группа:");

		sq = new SQLite();
		work = new Work();
		listQuestions = new ArrayList<String>();
		listAnswers = new ArrayList<String>();
		listTrueAnswers = new ArrayList<String>();
		listTrueAnswersStudet = new ArrayList<String>();
		resultList = new ArrayList<String>();
		random = new ArrayList<Integer>();
		sq.read(listQuestions, listAnswers, listTrueAnswers);
		work.Random(random);
		this.InitializeComponentRegistration(true);
		this.InitializeComponentResult(false);
		this.AddActionListenerRegistration();
		this.AddActionListenerStart();

	}

	/**
	 * Инициализируем компоненты для регистрации пользователя. Метод принимает
	 * логическую переменную, которая указывает на видимость
	 * элементов(true-видны,false-не видны)
	 * 
	 * @param b
	 *            - логическая переменная
	 */
	public void InitializeComponentRegistration(boolean b) {
		// окно
		frame.setVisible(true);
		frame.setSize(270, 200);
		frame.setLocationRelativeTo(null);// начальная позиция окна в центре
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// текстовые поля
		fname.setBounds(80, 10, 150, 20);
		fname.setVisible(b);
		frame.add(fname);

		lname.setBounds(80, 40, 150, 20);
		lname.setVisible(b);
		frame.add(lname);

		group.setBounds(80, 70, 150, 20);
		group.setVisible(b);
		frame.add(group);

		// кнопка
		ok.setBounds(160, 130, 80, 25);
		ok.setVisible(b);
		frame.add(ok);

		// Label
		lbFName.setBounds(40, 10, 70, 15);
		lbFName.setVisible(b);
		frame.add(lbFName);

		lbLName.setBounds(10, 40, 70, 15);
		lbLName.setVisible(b);
		frame.add(lbLName);

		lbGroup.setBounds(25, 70, 70, 15);
		lbGroup.setVisible(b);
		frame.add(lbGroup);
	}

	/**
	 * Обработка нажатия кнопки ok. При нажатии кнопки завершается процесс
	 * регистрации и начинается процесс прохождения теста.
	 */
	public void AddActionListenerRegistration() {
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				InitializeComponentRegistration(false);
				InitializeComponentTest(true);
				sb.append(fname.getText() + " " + lname.getText() + "\n"
						+ group.getText());

			}
		});
	}

	/**
	 * Инициализируем компоненты для прохождения теста пользователя. Метод
	 * принимает логическую переменную, которая указывает на видимость
	 * элементов(true-видны,false-не видны)
	 * 
	 * @param b
	 *            - логическая переменная
	 */
	public void InitializeComponentTest(boolean b) {
		// окно
		frame.setSize(600, 400);
		frame.setLocationRelativeTo(null);

		// кнопки
		answer1.setBounds(5, 210, 475, 25);
		answer1.setVisible(b);
		frame.add(answer1);

		answer2.setBounds(5, 245, 475, 25);
		answer2.setVisible(b);
		frame.add(answer2);

		answer3.setBounds(5, 280, 475, 25);
		answer3.setVisible(b);
		frame.add(answer3);

		answer4.setBounds(5, 315, 475, 25);
		answer4.setVisible(b);
		frame.add(answer4);

		start.setBounds(490, 5, 80, 30);
		start.setVisible(b);
		frame.add(start);

		// текстовое поле
		tf.setBounds(5, 5, 100, 100);
		tf.setVisible(b);
		frame.add(tf);

	}

	/**
	 * Инициализируем компоненты для получения результата о прохождении теста.
	 * Метод принимает логическую переменную, которая указывает на видимость
	 * элементов(true-видны,false-не видны)
	 * 
	 * @param b
	 *            - логическая переменная
	 * 
	 */
	public void InitializeComponentResult(boolean b) {
		result.setBounds(5, 5, 570, 350);
		result.setVisible(b);
		frame.add(result);
	}

	/**
	 * Принимает в качестве параметра кнопку. Используется в методе
	 * AddActionListenerTest Записывает в список выбранный вариант ответа
	 * Записывает в список результат текущего ответа(правильно или нет) Если
	 * следующего вопроса нет записывает результат тестировния в базу данных
	 * SQLite
	 * 
	 * @param b
	 *            - идентификатор кнопки
	 * @param after
	 *            - время ответа
	 */
	public void NextQuestion(Button b, long after) {
		if (i < 4) {
			i++;
			int r = random.get(i);
			long diff = after - before;
			listTrueAnswersStudet.add(b.getLabel());
			tf.setText(listQuestions.get(r));
			before = System.currentTimeMillis();

			resultList.add("Ваш ответ:"
					+ listTrueAnswersStudet.get(i - 1)
					+ "\t"
					+ work.CompareString(listTrueAnswersStudet.get(i - 1),
							listTrueAnswers.get(random.get(i - 1)))
					+ " Затраченное время: " + (double) diff / 1000 + " сек "
					+ "\n" + listQuestions.get(r) + "\n");
			Substring(listAnswers.get(r));

		} else {

			long diff = after - before;
			listTrueAnswersStudet.add(b.getLabel());
			resultList
					.add("Ваш ответ:"
							+ listTrueAnswersStudet.get(i)
							+ "\t"
							+ work.CompareString(listTrueAnswersStudet.get(i),
									listTrueAnswers.get(random.get(i)))
							+ " Затраченное время: " + (double) diff / 1000
							+ " сек \n");
			listTrueAnswersStudet.add(b.getLabel());
			InitializeComponentTest(false);
			InitializeComponentResult(true);
			sq.write(DBname, sb.toString(), resultList, Mark());
			result.setText(sq.ReadResult(DBname, sb.toString()));
		}
	}

	/**
	 * Обработка нажатий кнопок выбора правильного варианат ответа
	 * 
	 */
	public void AddActionListenerTest() {

		answer1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				long after = System.currentTimeMillis();
				NextQuestion(answer1, after);
			}
		});

		answer2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				long after = System.currentTimeMillis();
				NextQuestion(answer2, after);
			}
		});
		answer3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				long after = System.currentTimeMillis();
				NextQuestion(answer3, after);
			}
		});
		answer4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				long after = System.currentTimeMillis();
				NextQuestion(answer4, after);
			}
		});
	}

	/**
	 * Обработка нажатия кнопки старт При нажатии кнопки начинается прохождении
	 * теста
	 */
	public void AddActionListenerStart() {

		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				AddActionListenerTest();
				int r = random.get(0);
				tf.setText(listQuestions.get(r));
				Substring(listAnswers.get(r));
				resultList.add(listQuestions.get(r) + "\n");
				before = System.currentTimeMillis();
			}
		});
	}

	/**
	 * Разбиваем сторку на подстроки. Каждую подстроку записываем в каждую
	 * кнопку
	 * 
	 * @param s
	 *            - строка
	 */
	public void Substring(String s) {
		answer1.setLabel(s.substring(3, s.indexOf("\n")));
		int n = s.indexOf("\n");
		answer2.setLabel(s.substring(s.indexOf("b)", n + 1) + 3,
				s.indexOf("\n", n + 1)));
		n = s.indexOf("\n", n + 1);
		answer3.setLabel(s.substring(s.indexOf("c)", n + 1) + 3,
				s.indexOf("\n", n + 1)));
		answer4.setLabel(s.substring(s.indexOf("d") + 3, s.length()));

	}

	/**
	 * Возвращает оценку о прохождении теста
	 * 
	 * @return mark - оценка
	 */
	public int Mark() {
		int mark = 0;
		for (int i = 0; i < 5; i++) {
			int r = random.get(i);
			if (listTrueAnswers.get(r).equals(listTrueAnswersStudet.get(i))) {
				mark++;
			}
		}
		return mark;
	}
}
