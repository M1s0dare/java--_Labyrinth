// �K�v�ȃ��C�u�����̃C���|�[�g
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import javax.swing.*;

public class Labyrinth extends JFrame implements MouseListener,MouseMotionListener {
    // �Q�[���̊�{�v�f���i�[����t�B�[���h�ϐ�
    private JButton room[][];           // ������\���{�^����2�����z��
    private JButton wallvertical[][];   // �c�̕ǂ�\���{�^����2�����z��
    private JButton wallhorizontal[][]; // ���̕ǂ�\���{�^����2�����z��
    JLabel theLabelA;                   // ���x���p�ϐ�
    private Container c;                // �R���e�i�p�ϐ�
    PrintWriter out;                    // �T�[�o�[�ւ̏o�͗p���C�^�[
    
    // �Q�[���̏�Ԃ��Ǘ�����J�E���^�[
    private int entranceCount = 0;      // �����̐ݒu�񐔂��J�E���g
    private int exitCount = 0;          // �o���̐ݒu�񐔂��J�E���g
    private int wallCount = 0;          // �ݒu���ꂽ�ǂ̐����J�E���g
    
    // �Q�[���̏�����Ԃ��Ǘ�����t���O
    private boolean isReady = false;        // �����̏������
    private boolean opponentReady = false;  // ����̏������
    
    // �Q�[���Ŏg�p����摜�A�C�R��
    private ImageIcon background, entrance, exit;           // �w�i�A�����A�o���̃A�C�R��
    private ImageIcon right, left, up, down;               // �����������A�C�R��
    private ImageIcon walloff, wallon;                     // �ǂ̏�Ԃ������A�C�R��
    
    // ���݂̓����Əo���̈ʒu��ێ�
    private JButton currentEntrance = null;  // ���݂̓����{�^��
    private JButton currentExit = null;      // ���݂̏o���{�^��
    
    // ���H�̍\�����Ǘ�����f�[�^�\��
    private int[][] grid;                    // ���H�̏�Ԃ�ێ�����2�����z��
    private ArrayList<int[]> walls;          // �ǂ̈ʒu����ێ����郊�X�g
    private int[] startPosition;             // �����̈ʒu���W
    private int[] endPosition;               // �o���̈ʒu���W
    
    // �Q�[���i�s�Ɋւ���v�f
    private GameScreen gameScreen;           // �Q�[����ʂ̊Ǘ��p�I�u�W�F�N�g
    private JButton readyButton;             // ���������{�^��
    private String myName;                   // �v���C���[�̖��O
    private boolean isFirstPlayer = false;   // ��s�v���C���[���ǂ���
    private boolean isMyTurn = false;        // �����̃^�[�����ǂ���
    
    // �w�i�摜�̊Ǘ�
    private ImageIcon firstBackgroundImage;   // �ŏ��̉�ʂ̔w�i�摜
    private ImageIcon secondBackgroundImage;  // �ΐ��ʂ̔w�i�摜

    public Labyrinth() {
        // �O���b�h�̏������i11x11�j
        grid = new int[11][11];
        walls = new ArrayList<>();

        //���O�̓��̓_�C�A���O���J��
        this.myName = JOptionPane.showInputDialog(null,"���O����͂��Ă�������","���O�̓���",JOptionPane.QUESTION_MESSAGE);
        String ipadress = JOptionPane.showInputDialog(null,"IP�A�h���X����͂��Ă�������","IP�A�h���X�̓���",JOptionPane.QUESTION_MESSAGE);
        if(this.myName.equals("")){
            this.myName = "No name";//���O���Ȃ��Ƃ��́C"No name"�Ƃ���
        }
        if(ipadress.equals("")){
            ipadress = "localhost";//IP�A�h���X�����͂���Ȃ���"Localhost"�ɐڑ�
        }

        //�E�B���h�E���쐬����
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Labyrinth");
        setSize(2000,1200);
        setLayout(null);

        // �w�i�摜�̓ǂݍ���
        firstBackgroundImage = new ImageIcon(new File("fastbackground.png").getAbsolutePath());
        secondBackgroundImage = new ImageIcon(new File("secondbackground.png").getAbsolutePath());

        // �w�i�p�l���̍쐬�Ɛݒ�
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // �w�i�摜��`��
                g.drawImage(firstBackgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);
        backgroundPanel.setBounds(0, 0, 2000, 1200);
        backgroundPanel.setOpaque(false);

        // �����̃R���|�[�l���g��w�i�p�l���ɒǉ�
        c = backgroundPanel;

        // �w�i�p�l�����t���[���ɒǉ�
        getContentPane().add(backgroundPanel);

        //�A�C�R���̐ݒ�
        background = new ImageIcon(new File("background.png").getAbsolutePath());
        entrance = new ImageIcon(new File("in.png").getAbsolutePath());
        exit = new ImageIcon(new File("out.png").getAbsolutePath());
        walloff = new ImageIcon(new File("walloff.png").getAbsolutePath());
        wallon = new ImageIcon(new File("wallon.png").getAbsolutePath());
        
        // �O���b�h�̏�����
        for(int i = 0; i < 11; i++) {
            for(int j = 0; j < 11; j++) {
				if (i%2!=0 && j%2!=0) {
					grid[i][j] = 1; // �󔒂̏ꏊ�͕ǂƂ��Ă�����
				} else{
					grid[i][j] = 0;
				}
            }
        }
		

        c.setLayout(null);
        setupMazeButtons();
        setupWalls();
        setupRightPanel();
        setupReadyButton();
        setupServerConnection(myName, ipadress);
    }

    // ���H�̃{�^���ݒ�
    private void setupMazeButtons() {
        room = new JButton[6][6];
        for(int j=0;j<6;j++){
            for(int i=0;i<6;i++){
                room[j][i] = new JButton(background);
                c.add(room[j][i]);
                room[j][i].setBounds(i*110+100,j*110+100,100,100);
                room[j][i].addMouseListener(this);
                room[j][i].setActionCommand(Integer.toString(j*6+i));
            }
        }
		
		//���{�̔ԍ��iA-F,1-6�j�̕�����`��
		for(int i=0;i<6;i++){//1-6�̕�����`��
			JLabel theLabelA = new JLabel(String.valueOf(i + 1)); // ������String�ɕϊ�
			theLabelA.setFont(new Font("Arial", Font.BOLD, 30)); // �t�H���g�T�C�Y��30�ɐݒ�
            theLabelA.setBounds(i * 110 + 145, 50, 100, 30); // �e���x�������ɔz�u
            c.add(theLabelA); // �R���e�i�Ƀ��x����ǉ�
		}
		for(int j=0;j<6;j++){//A-F�̕�����`��
			JLabel theLabelA = new JLabel(String.valueOf((char) ('A' + j))); // 'A'���珇�ɕ����𐶐�
			theLabelA.setFont(new Font("Arial", Font.BOLD, 30)); // �t�H���g�T�C�Y��30�ɐݒ�
            theLabelA.setBounds(50, j * 110 + 140, 100, 30); // �e���x�������ɔz�u
            c.add(theLabelA); // �R���e�i�Ƀ��x����ǉ�
		}
    }

    // �ǃ{�^���̐ݒ�
    private void setupWalls() {
        wallvertical = new JButton[6][5];//�����̕ǁi�c�j
        for(int j=0;j<6;j++){
            for(int i=0;i<5;i++){
                wallvertical[j][i] = new JButton(walloff);
                c.add(wallvertical[j][i]);
                wallvertical[j][i].setBounds(i*110+200,j*110+100,10,100);
                wallvertical[j][i].addMouseListener(this);
                wallvertical[j][i].setActionCommand(Integer.toString(j*5+i+100));
                wallvertical[j][i].setOpaque(true);
                wallvertical[j][i].setContentAreaFilled(true);
                wallvertical[j][i].setBorderPainted(true);
            }
        }

        wallhorizontal = new JButton[5][6];//�����̕ǁi���j
        for(int j=0;j<5;j++){
            for(int i=0;i<6;i++){
                wallhorizontal[j][i] = new JButton(walloff);
                c.add(wallhorizontal[j][i]);
                wallhorizontal[j][i].setBounds(i*110+100,j*110+200,100,10);
                wallhorizontal[j][i].addMouseListener(this);
                wallhorizontal[j][i].setActionCommand(Integer.toString(j*6+i+200));
                wallhorizontal[j][i].setOpaque(true);
                wallhorizontal[j][i].setContentAreaFilled(true);
                wallhorizontal[j][i].setBorderPainted(true);
            }
        }
    }
	
	

    // �E�p�l���̐ݒ�
    private void setupRightPanel() {
        // 1. �����Əo���̌��o��
        theLabelA = new JLabel("1.�����Əo����ݒ�");
        theLabelA.setFont(new Font("Meiryo", Font.BOLD, 50));
        theLabelA.setBounds(960, 100, 500, 80);
        c.add(theLabelA);

        // ������
        theLabelA = new JLabel("<html>���{�̃}�X���N���b�N����ƁC�����Əo����ݒ�ł��܂��D<br>�܂��C���݂ɒu�������邱�Ƃ��ł��܂��D</html>");
        theLabelA.setFont(new Font("Yu Mincho Regular", Font.BOLD, 30));
        theLabelA.setBounds(990, 170, 900, 130);
        c.add(theLabelA);

        // 2. �ǂ̐ݒ�
        theLabelA = new JLabel("2.�ǂ̐ݒ�");
        theLabelA.setFont(new Font("Meiryo", Font.BOLD, 50));    
        theLabelA.setBounds(960, 320, 500, 80);
        c.add(theLabelA);

        // ������
        theLabelA = new JLabel("<html>���{�̕ǂ��N���b�N����ƁC�ǂ�ݒ�ł��܂��D<br>20���܂Őݒu�\�ł����C�U���ł���悤�z�u���Ă��������D</html>");
        theLabelA.setFont(new Font("Yu Mincho Regular", Font.BOLD, 30));
        theLabelA.setBounds(990, 390, 900, 130);
        c.add(theLabelA);

        // 3. ��������
        theLabelA = new JLabel("3.��������");
        theLabelA.setFont(new Font("Meiryo", Font.BOLD, 50));
        theLabelA.setBounds(960, 550, 500, 80);
        c.add(theLabelA);

        // ������
        theLabelA = new JLabel("<html>����������������C���������������Ă��������D<br>����̏�������������ƁC�Q�[�����n�܂�܂��D<br><font color='red'>�Ȃ��C��ɏ������ł����v���C���[�ɐ�s�̌������^�����܂��D</font></html>");
        theLabelA.setFont(new Font("Yu Mincho Regular", Font.BOLD, 30));
        theLabelA.setBounds(990, 620, 1100, 150);
        c.add(theLabelA);
    }

    // ���������{�^���̐ݒ�
    private void setupReadyButton() {
        readyButton = new JButton("��������");
        readyButton.setFont(new Font("Meiryo", Font.BOLD, 30));
        readyButton.setBounds(1100, 800, 200, 100);
        readyButton.setEnabled(false); // ������Ԃł͖������C�����Əo����ݒ肷��Ɖ�����悤��
        c.add(readyButton);
        readyButton.addMouseListener(this);
        readyButton.setActionCommand("ready");//�{�^���������ꂽ��ready�R�}���h�𑗐M
    }

    // �T�[�o�[�ڑ��̐ݒ�
    private void setupServerConnection(String myName, String ipadress) {
        Socket socket = null;
        try {
            socket = new Socket(ipadress, 10000);
            MesgRecvThread mrt = new MesgRecvThread(socket, myName);
            mrt.start();
        } catch (UnknownHostException e) {
            System.err.println("�z�X�g�� IP �A�h���X������ł��܂���: " + e);
        } catch (IOException e) {
            System.err.println("�G���[���������܂���: " + e);
        }
    }

    // ���C�����\�b�h
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                Labyrinth net = new Labyrinth();
                net.setVisible(true);
            } catch (Exception e) {
                System.err.println("�G���[���������܂���: " + e);
                e.printStackTrace();
            }
        });
    }

    // �Q�[����ʂւ̐؂�ւ����\�b�h
	private synchronized void switchToGameScreen() {
		// �Q�[����ʂւ̐؂�ւ����s��
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					// ���݂̃R���e�i���擾
					Container contentPane = getContentPane();
					
					// ��ʏ�̑S�ẴR���|�[�l���g���폜
					contentPane.removeAll();
					
					// �Q�[����ʂ��܂���������Ă��Ȃ��ꍇ�A�V��������
					if (gameScreen == null) {
						gameScreen = new GameScreen(room, out, myName);
						
						// �Q�[����ʂ̈ʒu�ƃT�C�Y��ݒ�
						gameScreen.setBounds(0, 0, 2000, 1200);
					}
					
					// �Q�[����ʂ��R���e�i�ɒǉ�
					contentPane.add(gameScreen);
					
					// ���C�A�E�g���Čv�Z
					validate();
					
					// �ĕ`����s��
					repaint();
					
					// �f�o�b�O�p�̃��b�Z�[�W��\��
					//System.out.println("�Q�[����ʂɈڍs����");
				} catch (Exception e) {
					// �G���[�����������ꍇ�̏���
					System.out.println("���̗��R�ŉ�ʈȍ~�ł��܂���ł���: " + e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}

    // MouseListener�̎����i���ׂ����������瓮��s���|�C���g�j
    public void mouseClicked(MouseEvent e) {
        JButton theButton = (JButton)e.getComponent();//�R���|�[�l���g�擾
        String theArrayIndex = theButton.getActionCommand();//�R�}���h�擾
        Icon theIcon = theButton.getIcon();//�A�C�R���擾
        
        if (isRoomButton(theButton)) {//room�{�^���������ꂽ�Ƃ�
            handleRoomButtonClick(theButton, theIcon);//�����܂��͏o����ݒ�
        } else if (isWallButton(theButton)) {//wall�{�^���������ꂽ�Ƃ�
            handleWallButtonClick(theButton, theArrayIndex);
        } else if (theButton.getActionCommand().equals("ready")) {//���������{�^���������ꂽ�Ƃ�
            handleReadyButtonClick();
        }
    }
	
	//�����{�^���������ꂽ�Ƃ��̏���
    private void handleRoomButtonClick(JButton theButton, Icon theIcon) {
        if (theIcon == background) {
            // �z�u��̍��W���v�Z
            int buttonIndex = Integer.parseInt(theButton.getActionCommand());
            int roomX = buttonIndex % 6;
            int roomY = buttonIndex / 6;
            int gridX = roomX * 2;
            int gridY = roomY * 2;
            
            // �l���̕ǂ��`�F�b�N�i��ɍs���j
            if (isSurroundedByWalls(gridX, gridY)) {
                JOptionPane.showMessageDialog(null, "�l����ǂň͂܂ꂽ�ꏊ�ɂ͐ݒu�ł��܂���", "�G���[", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (entranceCount == 0) {//�G���g�����X�J�E���g�O�i���͓�����u���j 
                if (currentEntrance != null && currentExit != null) {//�����Əo�����ݒ�ς݂̏ꍇ
                    // ���̃O���b�h��Ԃ�ۑ�
                    int[][] tempGrid = copyGrid(grid);
                    
                    // ���̓����Əo���̈ʒu���擾
                    int startX = getGridX(currentEntrance);
                    int startY = getGridY(currentEntrance);
                    int endX = getGridX(currentExit);
                    int endY = getGridY(currentExit);
                    
                    // �V���������̈ʒu�����ɐݒ�
                    tempGrid[gridY][gridX] = 2; // �V��������
                    tempGrid[startY][startX] = 0; // ���̓��������Z�b�g
                    
                    // �V������������o���ւ̌o�H�����݂��邩�m�F
                    if (isPathExistOnGrid(tempGrid, gridX, gridY, endX, endY)) {
                        // �o�H�����݂���ꍇ�A���ۂɃO���b�h���X�V
                        currentEntrance.setIcon(background);
                        resetGridPosition(currentEntrance);
                        theButton.setIcon(entrance);
                        currentEntrance = theButton;
                        updateGridPosition(theButton, 2); // grid 2 ������������킷
                        setStartPosition(theButton);
                        entranceCount = 1 - entranceCount;
                    } else {
                        JOptionPane.showMessageDialog(null, "�����ɂ͓�����u���܂���", "�G���[", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // ���߂ē�����ݒu����ꍇ
                    theButton.setIcon(entrance);
                    currentEntrance = theButton;
                    updateGridPosition(theButton, 2); // grid 2 ������������킷
                    setStartPosition(theButton);
                    entranceCount = 1 - entranceCount;
                }
            } else {//�o���̐ݒ�
                if (currentExit != null && currentEntrance != null) {//�o�����ݒ肳��Ă���ꍇ�́C�o�����Z�b�g
                    // ���̃O���b�h��Ԃ�ۑ�
                    int[][] tempGrid = copyGrid(grid);
                    
                    // ���̓����Əo���̈ʒu���擾
                    int startX = getGridX(currentEntrance);
                    int startY = getGridY(currentEntrance);
                    int endX = getGridX(currentExit);
                    int endY = getGridY(currentExit);
                    
                    // �V�����o���̈ʒu�����ɐݒ�
                    tempGrid[gridY][gridX] = 3; // �V�����o��
                    tempGrid[endY][endX] = 0; // ���̏o�������Z�b�g
                    
                    // ��������V�����o���ւ̌o�H�����݂��邩�m�F
                    if (isPathExistOnGrid(tempGrid, startX, startY, gridX, gridY)) {
                        // �o�H�����݂���ꍇ�A���ۂɃO���b�h���X�V
                        currentExit.setIcon(background);
                        resetGridPosition(currentExit);
                        theButton.setIcon(exit);
                        currentExit = theButton;
                        updateGridPosition(theButton, 3); // grid 3 ���o��������킷
                        setEndPosition(theButton);
                        entranceCount = 1 - entranceCount;
                    } else {
                        JOptionPane.showMessageDialog(null, "�����ɂ͏o����u���܂���", "�G���[", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // ���߂ďo����ݒu����ꍇ
                    theButton.setIcon(exit);
                    currentExit = theButton;
                    updateGridPosition(theButton, 3); // grid 3 ���o��������킷
                    setEndPosition(theButton);
                    entranceCount = 1 - entranceCount;
                }
            }
        } else if (theIcon == entrance || theIcon == exit) {
            JOptionPane.showMessageDialog(null, "�d�˂Ĕz�u�͂ł��܂���", "�G���[", JOptionPane.ERROR_MESSAGE);
        }
        updateReadyButtonState();//���������{�^����������̂��ǂ������X�V
    }

    // �O���b�h���R�s�[����w���p�[���\�b�h
    private int[][] copyGrid(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    // �{�^������O���b�h��X���W���擾����w���p�[���\�b�h
    private int getGridX(JButton button) {
        int buttonIndex = Integer.parseInt(button.getActionCommand());
        int roomX = buttonIndex % 6;
        return roomX * 2;
    }

    // �{�^������O���b�h��Y���W���擾����w���p�[���\�b�h
    private int getGridY(JButton button) {
        int buttonIndex = Integer.parseInt(button.getActionCommand());
        int roomY = buttonIndex / 6;
        return roomY * 2;
    }

    // ����̃O���b�h���W���l����ǂɈ͂܂�Ă��邩�`�F�b�N���郁�\�b�h
    private boolean isSurroundedByWalls(int gridX, int gridY) {
        int surroundingWalls = 0;
        
        // ��̕ǂ��`�F�b�N
        if (gridY > 0 && grid[gridY-1][gridX] == 1) surroundingWalls++;
        
        // ���̕ǂ��`�F�b�N
        if (gridY < 10 && grid[gridY+1][gridX] == 1) surroundingWalls++;
        
        // ���̕ǂ��`�F�b�N
        if (gridX > 0 && grid[gridY][gridX-1] == 1) surroundingWalls++;
        
        // �E�̕ǂ��`�F�b�N
        if (gridX < 10 && grid[gridY][gridX+1] == 1) surroundingWalls++;
        
        return surroundingWalls >= 3; // 3�ȏ�̕ǂň͂܂�Ă���ꍇ��true
    }

    // �w�肳�ꂽ�O���b�h��Ōo�H�����݂��邩�m�F���郁�\�b�h
    private boolean isPathExistOnGrid(int[][] checkGrid, int startX, int startY, int endX, int endY) {
        boolean[][] visited = new boolean[11][11];
        Queue<int[]> queue = new LinkedList<>();
        
        queue.offer(new int[]{startX, startY});
        visited[startY][startX] = true; // ����: Y, X�̏��ԂŔz��C���f�b�N�X���g�p
        
        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];
            
            if (x == endX && y == endY) {
                return true; // �ړI�n�ɓ��B
            }
            
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                
                if (isInsideGrid(nx, ny) && !visited[ny][nx]) {
                    if (checkGrid[ny][nx] == 0 || checkGrid[ny][nx] == 3) {
                        queue.offer(new int[]{nx, ny});
                        visited[ny][nx] = true;
                    }
                }
            }
        }
        
        return false; // �o�H��������Ȃ�
    }

    // �O���b�h���̍��W���ǂ������m�F���郁�\�b�h�i���\�b�h������薾�m�Ɂj
    private boolean isInsideGrid(int x, int y) {
        return x >= 0 && x < grid[0].length && y >= 0 && y < grid.length;
    }


	//�ǂ̃{�^���������ꂽ�Ƃ��̔���
    private void handleWallButtonClick(JButton theButton, String theArrayIndex) {//wall�{�^���N���b�N���C�{�^���̍��W��toggleWall�Ɋi�[
        if (currentEntrance != null && currentExit != null) {//�����Əo�����ݒ肳��Ă���Ƃ��ɕǂ�u����
            if (Integer.parseInt(theArrayIndex) >= 200) {//���̕ǂ��ǂ����𔻒�
                int row = (Integer.parseInt(theArrayIndex)-200) / 6;
                int col = (Integer.parseInt(theArrayIndex)-200) % 6;
                toggleWall(theButton, row, col, false);//toggleWall��false��
            } else {//�c�̕ǂł���Ƃ�
                int row = (Integer.parseInt(theArrayIndex)-100) / 5;
                int col = (Integer.parseInt(theArrayIndex)-100) % 5;
                toggleWall(theButton, row, col, true);//toggleWall��true��
            }
			//�X�^�[�g�ƃS�[���̍��W�������Ɍv�Z�i�ǂ����ɓ����ł��Ȃ����ȁH�j
            String currentEntranceArrayIndexStr = currentEntrance.getActionCommand();
            int currentEntranceArrayIndex = Integer.parseInt(currentEntranceArrayIndexStr);
            int startX = (currentEntranceArrayIndex % 6);
            int startY = (currentEntranceArrayIndex / 6);
            String currentExitArrayIndexStr = currentExit.getActionCommand();
            int currentExitArrayIndex = Integer.parseInt(currentExitArrayIndexStr);
            int endX = (currentExitArrayIndex % 6);
            int endY = (currentExitArrayIndex / 6);
			/*
            System.out.println("handleWallButtonClick");
            System.out.println("currentEntranceArrayIndex:"+currentEntranceArrayIndex+", currentExitArrayIndex:"+currentExitArrayIndex);
            System.out.println("startX:"+startX+", startY:"+startY);
            System.out.println("endX:"+endX+", endY:"+endY);
            System.out.println("entranceCount:"+entranceCount);*/
        }
    }
	
	//�����{�^���������ꂽ�Ƃ�
    private void handleReadyButtonClick() {
        if (currentEntrance == null || currentExit == null) {//�����Əo����ݒ肳����
            JOptionPane.showMessageDialog(null, "�����Əo����ݒ肵�Ă�������", "�G���[", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //System.out.println("�����{�^�����������F" + myName);

        // ���{���𕶎���ɕϊ�
        StringBuilder mazeInfo = new StringBuilder();//��������ǉ����Ă����邽�߁CStringBuilder�g�p�i�����Ƃ��Ė��{�Ǘ��j
        mazeInfo.append("MAZE_INFO ").append(myName).append(" ");//���{���{�v���C���[�̖��O�{��
        
        // �����Əo���̈ʒu��ǉ�
        if (currentEntrance != null) {
            String entrancePos = currentEntrance.getActionCommand();
            mazeInfo.append("E").append(entrancePos).append(" ");//E�ientrance�j�{�����̈ʒu�{��
        }
        if (currentExit != null) {
            String exitPos = currentExit.getActionCommand();
            mazeInfo.append("X").append(exitPos).append(" ");//X�iexit�j�{�o���̈ʒu�{��
        }
        
        // �ǂ̏���ǉ��i�璷�ɂȂ��Ă����̂��Q�s�ɂ܂Ƃ߂��j
		/*for (int i = 0; i < walls.length; i++){
			int[] wall = walls[i];
			// �ǂ�X���W��Y���W���ʂɎ擾
			int wallX = wall[0];
			int wallY = wall[1];
			mazeInfo.append("W").append(wallX).append(",").append(wallY).append(" ");
		}*/
        for (int[] wall : walls) {
            mazeInfo.append("W").append(wall[0]).append(",").append(wall[1]).append(" ");
        }
        
        // ���{���𑗐M
        out.println(mazeInfo.toString());
        out.flush();

        // ���������𑗐M
        out.println("READY " + myName);
        out.flush();
        isReady = true;
        
        // ��������s���ǂ�����ݒ�i���肪�܂������������Ă��Ȃ��ꍇ�͐�s�j
        isFirstPlayer = !opponentReady;
        isMyTurn = isFirstPlayer;
        
        // ���������{�^���𖳌���
        readyButton.setEnabled(false);
        
        // UI�X�V�i���̖͂������j
        disableInput();
        
        // �Q�[���J�n�̏���
        if (opponentReady) {
            // ���肪���ɏ��������Ȃ��U�Ƃ��ĊJ�n
            isFirstPlayer = false;
            isMyTurn = false;
            out.println("TURN_ORDER " + myName + " " + isFirstPlayer);
            out.flush();
            startGame();
        } else {
            // ���肪�܂������������Ă��Ȃ��ꍇ�͐�s�Ƃ��đҋ@
            isFirstPlayer = true;
            isMyTurn = true;
            SwingUtilities.invokeLater(() -> {// �^�C�g���F���������C���b�Z�[�W�F����̏�������������܂ł��҂���������
                JOptionPane.showMessageDialog(null, "����̏�������������܂ł��҂���������", "��������", JOptionPane.INFORMATION_MESSAGE);
            });
        }

    }

    private synchronized void startGame() {//����̉�ʂƂ̓����͂ł��Ȃ����߁Cthis��synchronized�Ŗ��������������ɂ��Ă݂��D�G���[���炯�C��������
        //System.out.println("startGame���Ăяo�����F " + myName);
        
        if (!isReady || !opponentReady) {//�����ł��Ă邩�ǂ���
            //System.out.println("�S�������ł��Ă��Ȃ��̂ŊJ�n�ł��܂���");
            return;
        }

        if (gameScreen != null) {// ���łɃQ�[�����J�n����Ă���ꍇ
            //System.out.println("�Q�[���͂��łɎn�܂��Ă��܂�");
            return;
        }

        SwingUtilities.invokeLater(() -> {// ���̏��������ƁCRunnable���Ȃ��Ă������C@Override����Ȃ��炵��
            try {
                //System.out.println("�Q�[����ʍX�V�J�n");
                
                String message = "����̏������������܂����B\n" +
								 "���Ȃ��͓�������J�n���āA�o����ڎw���܂��B";
                JOptionPane.showMessageDialog(null, message, "�Q�[���J�n", JOptionPane.INFORMATION_MESSAGE);
                
                // �Q�[����ʂ̐���
                Container contentPane = getContentPane();//�y�C���擾
                contentPane.removeAll();//��U�y�C���S���͂���
                gameScreen = new GameScreen(room, out, myName);//gameScreen����
                gameScreen.setBounds(0, 0, 2000, 1200);
                contentPane.add(gameScreen);//�y�C���ɒ���t��
                
                // �Q�[���J�n���b�Z�[�W�̕\��
                gameScreen.addChatMessage("�V�X�e��: �Q�[�����J�n���܂�");
                if (isFirstPlayer) {
                    gameScreen.addChatMessage("�V�X�e��: [ " + myName + " ]����s�ł�");
                    gameScreen.addChatMessage("�V�X�e��: [ " + myName + " ]����Q�[�����J�n���Ă�������");
                    gameScreen.isMyTurn = true;
                    gameScreen.enableMovement();
                } else {
                    gameScreen.addChatMessage("�V�X�e��: [ " + myName + " ]����U�ł�");
                    gameScreen.addChatMessage("�V�X�e��: ����v���C���[����Q�[�����J�n���܂�");
                    gameScreen.isMyTurn = false;
                    gameScreen.disableMovement();
                }
                
                validate();// ���C�A�E�g�X�V
                repaint();// �ĕ`��
                //System.out.println("�Q�[����ʂ̐ݒ芮�� " + (isFirstPlayer ? "Player1" : "Player2")); //���߂ɏ��������������v���C���[���m����
            } catch (Exception e) {
                System.out.println("�Q�[����ʂ̐ݒ蒆�ɃG���[: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void disableInput() {//���������{�^���������ꂽ�Ƃ��ɓ��͂𖳌�������
        // �����{�^���𖳌���
        for(int i = 0; i < room.length; i++) {
            for(int j = 0; j < room[i].length; j++) {
                room[i][j].setEnabled(false);
            }
        }
        // �c�̕ǃ{�^���𖳌���
        for(int i = 0; i < wallvertical.length; i++) {
            for(int j = 0; j < wallvertical[i].length; j++) {
                wallvertical[i][j].setEnabled(false);
            }
        }
        // ���̕ǃ{�^���𖳌���
        for(int i = 0; i < wallhorizontal.length; i++) {
            for(int j = 0; j < wallhorizontal[i].length; j++) {
                wallhorizontal[i][j].setEnabled(false);
            }
        }
    }

    // �����ꂽ�{�^���̔���p�C�����C�ǂ̏��i��t���������̂Ȃ̂œ������邩���j
    private boolean isRoomButton(JButton button) {//room�{�^���������ꂽ�Ƃ��̔���p
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (button == room[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean isWallButton(JButton button) {//wall�{�^���������ꂽ�Ƃ��̔���p
        // �c�ǂ̃`�F�b�N
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                if (button == wallvertical[i][j]) {
                    return true;
                }
            }
        }
        // ���ǂ̃`�F�b�N
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                if (button == wallhorizontal[i][j]) {
                    return true;
                }
            }
        }
        return false;//�ꉞfalse��Ԃ�
    }
	
    private void updateGridPosition(JButton button, int value) {//�V����grid�̍��W�Ƃ��̑����i������o���j��ݒ�
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (button == room[i][j]) {
                    grid[i*2][j*2] = value;//�����{�^�����N���b�N���ꂽ�Ƃ��Cgrid��value��ݒ�
                    break;
                }
            }
        }
    }

    private void resetGridPosition(JButton button) {//grid��0�i�����Ȃ��ʘH�Cbackground�j�ɕύX
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (button == room[i][j]) {
                    grid[i*2][j*2] = 0;//grid 0�i�ʘH�j�ɕύX
                    break;
                }
            }
        }
    }

    private void setStartPosition(JButton button) {//�J�n�ʒu��ݒ�C��ŕ��D��T���ɂ�������
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (button == room[i][j]) {
                    startPosition = new int[]{i, j};
                    break;
                }
            }
        }
    }

    private void setEndPosition(JButton button) {//�S�[���ʒu��ݒ�C��ŕ��D��T���ɂ�������
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (button == room[i][j]) {
                    endPosition = new int[]{i, j};
                    break;
                }
            }
        }
    }
	//mouseListener�̐ݒ�̂��ߕK�v
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}

	//�ǂɂ��Ă̐ݒ�i���荞�݁j
	private void toggleWall(JButton button, int buttonIndex, int col, boolean isVertical) {
		if (button.getIcon() == walloff) {//�����C�N���b�N�����ꏊ�ɕǂ��ݒ肳��Ă��Ȃ����
			if (wallCount >= 20) {//�ǂ�20���ȏ゠�����炻��ȏエ���Ȃ�
				JOptionPane.showMessageDialog(null, "�ǂ͍ő�20���܂ł����ݒu�ł��܂���", "�G���[", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (!canPlaceWall(buttonIndex, col, isVertical)) {//�ǂ��u���邩�ǂ����̔���
				JOptionPane.showMessageDialog(null, "�����ɂ͕ǂ�u���܂���", "�G���[", JOptionPane.ERROR_MESSAGE);
				return;
			}

			int baseIndex;//�ǂ̔��ʂ̂��߂ɁC���ꂼ��̐��l�ɑ�������
			if (isVertical) {
				baseIndex = 100;//�c��
			} else {
				baseIndex = 200;
			}

			int index;//�ǂ̃C���f�b�N�X�i�ʒu�j���v�Z
			if (isVertical) {
				index = (buttonIndex * 5 + col) + baseIndex;
			} else {
				index = (buttonIndex * 6 + col) + baseIndex;
			}

			int gridRow;//�ǂ̍s�̈ʒu�i�����Ɗ�ŋ�ʁj
			if (isVertical) {
				gridRow = buttonIndex * 2;
			} else {
				gridRow = buttonIndex * 2 + 1;
			}

			int gridCol;//�ǂ̗�̈ʒu�i�����Ɗ�ŋ�ʁj
			if (isVertical) {
				gridCol = col * 2 + 1;
			} else {
				gridCol = col * 2;
			}

			button.setIcon(wallon);
			grid[gridRow][gridCol] = 1;
			addWall(index, col);
			wallCount++;
			//�f�o�b�O�p
			if (isVertical){//�����̂Ƃ�
				System.out.println("�����̕�");
				System.out.println("index:"+index);
				System.out.println("gridRow:"+gridRow);
				System.out.println("gridCol:"+gridCol);
			} else {//�����̎�
				System.out.println("�����̕�");
				System.out.println("index:"+index);
				System.out.println("gridRow:"+gridRow);
				System.out.println("gridCol:"+gridCol);
			}
		} else {//�ǂ������Ƃ�
			int baseIndex;//��ł��������
			if (isVertical) {
				baseIndex = 100;
			} else {
				baseIndex = 200;
			}

			int index;//
			if (isVertical) {
				index = (buttonIndex * 5 + col) + baseIndex;
			} else {
				index = (buttonIndex * 6 + col) + baseIndex;
			}

			// �ǂ������ʒu��grid���W���v�Z
			int gridRow = buttonIndex * 2; // ��ƂȂ�s
			int gridCol = col * 2; // ��ƂȂ��

			if (isVertical) {
			    gridCol++; // �����̕ǂ͊��
			} else {
			    gridRow++; // �����̕ǂ͊�s
			}

			button.setIcon(walloff);
			grid[gridRow][gridCol] = 0;
			removeWall(index, col);
			wallCount--;
			/*
			//�f�o�b�O�p
			if (isVertical){//�����̕Ǎ폜
				System.out.println("�����̕Ǎ폜");
				System.out.println("index:"+index);
				System.out.println("gridRow:"+gridRow);
				System.out.println("gridCol:"+gridCol);
			} else {//�����̎�
				System.out.println("�����̕Ǎ폜");
				System.out.println("index:"+index);
				System.out.println("gridRow:"+gridRow);
				System.out.println("gridCol:"+gridCol);
			}
			*/
		}
		
	}

	//�ǂ�u���邩�ǂ����̔���
	private boolean canPlaceWall(int row, int col, boolean isVertical) {
		// �����Əo�����ݒ肳��Ă��Ȃ��ꍇ�͕ǂ�u���Ȃ�
		if (currentEntrance == null || currentExit == null) {
			return false;
		}

		// ���݂�grid�̏�Ԃ��ꎞ�ۑ�
		int[][] tempGrid = copyGrid(grid);

		// ���ɕǂ�ݒu
		// �ǂ�grid���W���v�Z
		int gridRow = row * 2; // ��ƂȂ�s��ݒ�
		int gridCol = col * 2; // ��ƂȂ���ݒ�

		if (isVertical) {
		    gridCol++; // �����̕ǂ͊��ɔz�u
		} else {
		    gridRow++; // �����̕ǂ͊�s�ɔz�u
		}

		tempGrid[gridRow][gridCol] = 1; // �ǂƂ��Đݒ�

		// �����Əo���̍��W���擾
		int startX = getGridX(currentEntrance);
		int startY = getGridY(currentEntrance);
		int endX = getGridX(currentExit);
		int endY = getGridY(currentExit);

		// �o�H�����݂��邩�m�F
		boolean hasPath = isPathExistOnGrid(tempGrid, startX, startY, endX, endY);

		return hasPath;
	}

	//canplace�ŒʘH���ʂ��Ă��邩�̊m�F�i�S�[���ł���ʘH���m�ۂ���Ă��邩�j
    private boolean isPathExist(int startX, int startY, int endX, int endY) {
        //printGrid();//�ǂ̈ʒu��\���i�f�o�b�O�p�j
        
        boolean[][] visited = new boolean[11][11];//visited�̏�����
        Queue<int[]> queue = new LinkedList<>();//���D��T���̃��X�g

        // �����ʒu���L���[�ɒǉ�
        queue.offer(new int[]{startX, startY});
        visited[startX][startY] = true;

        // �㉺���E�̈ړ������i11x11�O���b�h�ł̈ړ��j
        int[] dx = {0, 0, -1, 1}; // ���E�̈ړ���1�}�X�P��
        int[] dy = {-1, 1, 0, 0}; // �㉺�̈ړ���1�}�X�P��

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            // 4�����̒T��
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];

                // �ړ��悪���H�͈͓̔��ŁA���K�₩�ʉ߉\�ȏꍇ
                if (isInside(nx, ny) && !visited[nx][ny]) {
					if (grid[nx][ny] == 0) {
                        if (nx != startX || ny != startY) {
                            queue.offer(new int[]{nx, ny});
                            visited[nx][ny] = true;
                        }
					} else if (grid[nx][ny] == 3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
	/*
    // grid�̏�Ԃ�\���i�f�o�b�O�p�j
    private void printGrid() {
        System.out.println("grid:\n");
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
	*/
	
	// ���肪�g���Ɏ��܂��Ă��邩�̔���̂�
    private boolean isInside(int x, int y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }

	//�ǂ�ǉ����邾���̏���
    private void addWall(int x, int y) {
        if (!wallExists(x, y)) {
            walls.add(new int[]{x, y});
        }
    }

	//�ǂ����������̏���
    private void removeWall(int x, int y) {
        walls.removeIf(wall -> wall[0] == x && wall[1] == y);
    }

	//�ǂ����݂��邩�ǂ����̔�r�̂�
    private boolean wallExists(int x, int y) {
        for (int[] wall : walls) {
            if (wall[0] == x && wall[1] == y) {
                return true;
            }
        }
        return false;
    }

	//���������{�^���������邩�ǂ����̊m�F�̂�
    private void updateReadyButtonState() {
        readyButton.setEnabled(currentEntrance != null && currentExit != null);
    }

    // �Q�[����ʂ̎����i�N���X�̒��ɃN���X�������ł���̂��E�E�E�H�j
// ����̖��H�����ꎞ�ۑ����邽�߂̐ÓI�t�B�[���h
private static ArrayList<int[]> tempOpponentWalls = new ArrayList<>();
private static int[][] tempOpponentGrid = new int[11][11];
private static boolean hasTempMazeInfo = false;

// GameScreen�N���X: �Q�[����ʂ̎�v�R���|�[�l���g���Ǘ�����N���X
private class GameScreen extends JPanel {
    // �v���C���[�ƃQ�[����Ԃ��Ǘ�����ϐ�
    private JButton[][] leftRoom;        // �����̖��{�i���肪�U�����鑤�j�̃{�^���z��
    private JButton[][] rightRoom;       // �E���̖��{�i�������U�����鑤�j�̃{�^���z��
    private JTextArea chatArea;          // �`���b�g���b�Z�[�W��\������G���A
    private JTextField moveInput;         // �ړ��R�}���h�̓��̓t�B�[���h
    private JTextField chatInput;         // �`���b�g���b�Z�[�W�̓��̓t�B�[���h
    private JButton sendButton;          // �ړ��R�}���h�̑��M�{�^��
    private JButton chatButton;          // �`���b�g���b�Z�[�W�̑��M�{�^��
    private PrintWriter out;             // �T�[�o�[�ւ̃��b�Z�[�W���M�p
    private int playerX = -1;            // �v���C���[��X���W�i�����l-1�͖��z�u��ԁj
    private int playerY = -1;            // �v���C���[��Y���W�i�����l-1�͖��z�u��ԁj
    private int opponentX = -1;          // ����v���C���[��X���W
    private int opponentY = -1;          // ����v���C���[��Y���W
    private ImageIcon playerIcon;        // �����̃v���C���[�A�C�R���i�ԐF�j
    private ImageIcon opponentIcon;      // ����̃v���C���[�A�C�R���i�F�j
    private String myName;               // �v���C���[�̖��O
    private boolean gameEnded = false;   // �Q�[���I���t���O
    private boolean isMyTurn = true;     // �^�[���Ǘ��t���O
    private javax.swing.Timer moveTimer; // �ړ����̒x�������p�^�C�}�[
    private ArrayList<Point> moveHistory = new ArrayList<>(); // �ړ�������ۑ�
    private int[][] gameGrid;            // �Q�[���p�̖��H�O���b�h
    private ArrayList<int[]> opponentWalls = new ArrayList<>(); // ����̕Ǐ��
    private int[][] opponentGrid = new int[11][11];             // ����̖��H�O���b�h

    // �ǂ̔�����s�����\�b�h
    // ���݈ʒu����V�����ʒu�ւ̈ړ����\���ǂ������`�F�b�N
    private boolean checkWall(int newRow, int newCol) {
        // �O���b�h���W�ɕϊ��i6x6����11x11�ւ̕ϊ��j
        // �ʘH�ƕǂ��܂ޏڍׂȃO���b�h�ł̍��W���v�Z
        int currentRow = playerX * 2;    // ���݂̍s��11x11�O���b�h�̍��W�ɕϊ�
        int currentCol = playerY * 2;    // ���݂̗��11x11�O���b�h�̍��W�ɕϊ�
        int newGridRow = newRow * 2;     // �ړ���̍s��11x11�O���b�h�̍��W�ɕϊ�
        int newGridCol = newCol * 2;     // �ړ���̗��11x11�O���b�h�̍��W�ɕϊ�

        // �ړ����������i�㉺���E�̈ړ��ʂ��v�Z�j
        int dx = Integer.compare(newGridRow, currentRow);  // �s�����̈ړ��ʁi-1, 0, 1�j
        int dy = Integer.compare(newGridCol, currentCol);  // ������̈ړ��ʁi-1, 0, 1�j

        // ���݈ʒu����ړ���܂ł̌o�H���`�F�b�N
        boolean foundWall = false;       // �ǂ����邩�ǂ����̃t���O
        Point wallLocation = null;       // ���������ǂ̈ʒu
        int x = currentRow;              // �`�F�b�N�ʒu�i�s�j
        int y = currentCol;              // �`�F�b�N�ʒu�i��j

        // ���݈ʒu����ړ���܂�1�}�X���m�F
        while (x != newGridRow || y != newGridCol) {
            // ���̃}�X�̍��W���v�Z
            Point wallCheck = new Point(
                dx != 0 ? x + dx : x,    // �c�����̈ړ�������ꍇ�͍s���X�V
                dy != 0 ? y + dy : y     // �������̈ړ�������ꍇ�͗���X�V
            );

            // �ړ���̃}�X�ɕǂ����邩�`�F�b�N
            if (opponentGrid[wallCheck.x][wallCheck.y] == 1) {
                foundWall = true;        // �ǂ𔭌�
                wallLocation = wallCheck; // �ǂ̈ʒu���L�^
                break;                   // �ǂ�����������T���I��
            }

            // ���̃}�X�ֈړ�
            x += dx;
            y += dy;
        }

        // �ǂ����������ꍇ�A���̕ǂ�\��
        if (foundWall && wallLocation != null) {
            JButton wallBtn = wallButtons.get(wallLocation);
            if (wallBtn != null) {
                wallBtn.setIcon(wallon);         // �ǂ̃A�C�R����ݒ�
                wallBtn.setOpaque(true);         // �{�^����s������
                wallBtn.setContentAreaFilled(true);
                wallBtn.setBorderPainted(true);
                wallBtn.setVisible(true);        // �ǂ�\��
                visibleWalls.add(wallLocation);  // �\���ς݂̕ǃ��X�g�ɒǉ�
            }
        } else {
            // �ǂ��Ȃ��ꍇ�A�ړ��悪�o�����ǂ������`�F�b�N
            if (opponentGrid[newGridRow][newGridCol] == 3) {
                return false;    // �o���̏ꍇ�͈ړ��\
            }
        }
        return foundWall;    // �ǂ������ true�A�Ȃ���� false ��Ԃ�
    }

    public GameScreen(JButton[][] myMaze, PrintWriter out, String myName) {
        this.myName = myName;
        playerIcon = new ImageIcon(new File("myicon.png").getAbsolutePath());
        opponentIcon = new ImageIcon(new File("youricon.png").getAbsolutePath());
        this.out = out;
        setLayout(null);
        setOpaque(false);

        // �w�i�p��JLabel���쐬
        JLabel backgroundLabel = new JLabel(secondBackgroundImage) {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(secondBackgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundLabel.setBounds(0, 0, 2000, 1200);
        
        // ���C���p�l���i���̃R���|�[�l���g���܂ށj���쐬
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBounds(0, 0, 2000, 1200);
        mainPanel.setOpaque(false);

        // �w�i�ƃ��C���p�l����ǉ�
        add(backgroundLabel);
        add(mainPanel);
        
        // ���C���p�l���ɑ΂��ăR���|�[�l���g��ǉ�
        setupPanels(myMaze, mainPanel);
        setupChatArea(mainPanel);
        setupMoveInput(mainPanel);

        // ���C���[�̏�����ݒ�
        setComponentZOrder(mainPanel, 0);    // �O��
        setComponentZOrder(backgroundLabel, 1); // �w��
    }

    private void setupPanels(JButton[][] myMaze, JPanel parent) {
        // �{�^���T�C�Y������������
        int buttonSize = 80;
        int spacing = 90;

        // ���p�l���i���肪�U��������{�j
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBounds(100, 250, 700, 700);  // Y���W��250�ɕύX
        leftPanel.setBackground(Color.LIGHT_GRAY);
        
        // �E�p�l���i�������U��������{�j- �ʒu���E�Ɉړ�
        JPanel rightPanel = new JPanel(null);
        rightPanel.setBounds(1200, 250, 700, 700);
        rightPanel.setBackground(Color.LIGHT_GRAY);

        // ���W���x���̒ǉ��i���p�l���j
        for(int i=0; i<6; i++) {
            // �����i1-6�j
            JLabel numLabel = new JLabel(String.valueOf(i + 1));
            numLabel.setFont(new Font("Arial", Font.BOLD, 20));
            numLabel.setBounds(i * spacing + 45, 0, 30, 20);
            leftPanel.add(numLabel);

            // �A���t�@�x�b�g�iA-F�j
            JLabel alphaLabel = new JLabel(String.valueOf((char)('A' + i)));
            alphaLabel.setFont(new Font("Arial", Font.BOLD, 20));
            alphaLabel.setBounds(0, i * spacing + 45, 20, 20);
            leftPanel.add(alphaLabel);
        }

        // ���W���x���̒ǉ��i�E�p�l���j
        for(int i=0; i<6; i++) {
            // �����i1-6�j
            JLabel numLabel = new JLabel(String.valueOf(i + 1));
            numLabel.setFont(new Font("Arial", Font.BOLD, 20));
            numLabel.setBounds(i * spacing + 45, 0, 30, 20);
            rightPanel.add(numLabel);

            // �A���t�@�x�b�g�iA-F�j
            JLabel alphaLabel = new JLabel(String.valueOf((char)('A' + i)));
            alphaLabel.setFont(new Font("Arial", Font.BOLD, 20));
            alphaLabel.setBounds(0, i * spacing + 45, 20, 20);
            rightPanel.add(alphaLabel);
        }

        leftRoom = new JButton[6][6];
        rightRoom = new JButton[6][6];

        // ���p�l���i���肪�U��������{�j�ɂ͎������쐬�������{��\���i�ǂ���j
        initializeMaze(leftRoom, myMaze, leftPanel, true);
        
        // �E�p�l���i�������U��������{�j�ɂ͓����Əo���݂̂�\���i�ǂȂ��j
        initializeMaze(rightRoom, myMaze, rightPanel, false);

        parent.add(leftPanel);
        parent.add(rightPanel);
    }

    private Map<Point, JButton> wallButtons = new HashMap<>();
    private Set<Point> visibleWalls = new HashSet<>();
    
    private void initializeMaze(JButton[][] targetMaze, JButton[][] sourceMaze, JPanel panel, boolean showWalls) {
        // �{�^���T�C�Y������������
        int buttonSize = 80;  // 100����80�ɏk��
        int spacing = 90;     // 110����90�ɏk��

        // �܂��{�^����������
        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 6; j++) {
                targetMaze[i][j] = new JButton();
                targetMaze[i][j].setBounds(j*spacing+25, i*spacing+25, buttonSize, buttonSize);
                targetMaze[i][j].setEnabled(false);
                targetMaze[i][j].setBackground(new Color(240, 240, 240)); // ���邢�D�F�̔w�i
                targetMaze[i][j].setOpaque(true);
                targetMaze[i][j].setContentAreaFilled(true);
                targetMaze[i][j].setBorderPainted(true);
                // �A�C�R����O�ʂɕ\��
                targetMaze[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                targetMaze[i][j].setVerticalAlignment(SwingConstants.CENTER);
                targetMaze[i][j].setIconTextGap(0);
                panel.add(targetMaze[i][j]);
            }
        }

        // �E���̖��H�̏ꍇ�A�ǃ{�^����������
        if (!showWalls) {
            // �c�̕�
            for(int i = 0; i < 6; i++) {
                for(int j = 0; j < 5; j++) {
                    JButton wallButton = new JButton(walloff);
                    wallButton.setBounds(j*spacing+105, i*spacing+25, 10, buttonSize);
                    wallButton.setEnabled(false);
                    wallButton.setVisible(false);
                    panel.add(wallButton);
                    wallButtons.put(new Point(i*2, j*2+1), wallButton);
                }
            }

            // ���̕�
            for(int i = 0; i < 5; i++) {
                for(int j = 0; j < 6; j++) {
                    JButton wallButton = new JButton(walloff);
                    wallButton.setBounds(j*spacing+25, i*spacing+105, buttonSize, 10);
                    wallButton.setEnabled(false);
                    wallButton.setVisible(false);
                    panel.add(wallButton);
                    wallButtons.put(new Point(i*2+1, j*2), wallButton);
                }
            }
        }

        if (showWalls) {
            // �����̖��{�F�����̐ݒ��\��
            for(int i = 0; i < 6; i++) {
                for(int j = 0; j < 6; j++) {
                    Icon icon = sourceMaze[i][j].getIcon();
                    if (icon == entrance) {
                        targetMaze[i][j].setIcon(entrance);
                        targetMaze[i][j].setDisabledIcon(entrance); // ��������Ԃł������A�C�R����\��
                    } else if (icon == exit) {
                        targetMaze[i][j].setIcon(exit);
                        targetMaze[i][j].setDisabledIcon(exit); // ��������Ԃł������A�C�R����\��
                    }
                }
            }
            
            // �����̕ǂ�\��
            for (int[] wall : walls) {// �ǂ��ׂĂɏ�������ifor a:b)
                int wallX = wall[0];
                int wallY = wall[1];
                JButton wallButton = new JButton();
                wallButton.setIcon(wallon);
                wallButton.setEnabled(false);

                if (wallX >= 100 && wallX < 200) { // �����̕�
                    int row = (wallX - 100) / 5;
                    int col = (wallX - 100) % 5;
                    wallButton.setBounds(col*spacing+105, row*spacing+25, 10, buttonSize);
                } else if (wallX >= 200) { // �����̕�
                    int row = (wallX - 200) / 6;
                    int col = (wallX - 200) % 6;
                    wallButton.setBounds(col*spacing+25, row*spacing+105, buttonSize, 10);
                }
                panel.add(wallButton);
            }
        } else {
            // �E���̖��{�F����̐ݒ��\��
            for(int i = 0; i < 11; i += 2) {
                for(int j = 0; j < 11; j += 2) {
                    int value = tempOpponentGrid[i][j];  // tempOpponentGrid���g�p
                    int roomI = i/2;
                    int roomJ = j/2;
                    
                    if (value == 2) { // ����
                        targetMaze[roomI][roomJ].setIcon(entrance);
                        targetMaze[roomI][roomJ].setDisabledIcon(entrance);
                        // �v���C���[�̏����ʒu��ݒ�
                        if (playerX == -1 && playerY == -1) {
                            updatePlayerPosition(roomI, roomJ);
                        }
                    } else if (value == 3) { // �o��
                        targetMaze[roomI][roomJ].setIcon(exit);
                        targetMaze[roomI][roomJ].setDisabledIcon(exit);
                    }
                }
            }

            // �v���C���[�̌��݈ʒu��\��
            if (playerX != -1 && playerY != -1) {
                targetMaze[playerX][playerY].setIcon(playerIcon);
                targetMaze[playerX][playerY].setDisabledIcon(playerIcon);
            }

            // ����̕Ǐ���ݒ�
            opponentGrid = tempOpponentGrid.clone();
            opponentWalls.clear();
            opponentWalls.addAll(tempOpponentWalls);
        }

        // ���ۂ̕ǂ̈ʒu���L�^
        for (int[] wall : opponentWalls) {
            int wallX = wall[0];
            if (wallX >= 100 && wallX < 200) {
                int row = (wallX - 100) / 5;
                int col = (wallX - 100) % 5;
                opponentGrid[row*2][col*2+1] = 1;
            } else if (wallX >= 200) {
                int row = (wallX - 200) / 6;
                int col = (wallX - 200) % 6;
                opponentGrid[row*2+1][col*2] = 1;
            }
        }
    }

    // �`���b�g�G���A�̃Z�b�g�A�b�v
    // �`���b�g�̕\���̈�ƃ��b�Z�[�W���͗p��UI�R���|�[�l���g��ݒ�
    private void setupChatArea(JPanel parent) {
        // �`���b�g�G���A�̈ʒu�𒲐�
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setFont(new Font("Meiryo", Font.PLAIN, 16));
        
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBounds(850, 250, 300, 400);  // Y���W��250�ɕύX
        parent.add(scrollPane);

        chatInput = new JTextField();
        chatButton = new JButton("���M");
        
        // �`���b�g�{�^���̃A�N�V�������X�i�[��ݒ�
        chatButton.addActionListener(e -> {
            String message = chatInput.getText().trim();
            if (!message.isEmpty()) {
                // �T�[�o�[�Ƀ��b�Z�[�W�𑗐M
                out.println("CHAT " + myName + " " + message);
                out.flush();
                // �����̃��b�Z�[�W���`���b�g�G���A�ɕ\��
                addChatMessage("���Ȃ�: " + message);
                // ���̓t�B�[���h���N���A
                chatInput.setText("");
            }
        });

        // Enter�L�[�ł����b�Z�[�W�𑗐M�ł���悤�ɂ���
        chatInput.addActionListener(e -> chatButton.doClick());
        
        // �`���b�g���͕����̈ʒu�𒲐�
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBounds(850, 660, 300, 30);  // Y���W��660�ɕύX
        chatPanel.add(chatInput, BorderLayout.CENTER);
        chatPanel.add(chatButton, BorderLayout.EAST);
        parent.add(chatPanel);
    }

    // �ړ����͋@�\�̃Z�b�g�A�b�v
    // �v���C���[�̈ړ����߂���́E��������UI�R���|�[�l���g��ݒ�
    private void setupMoveInput(JPanel parent) {
        // �ړ����͕����̈ʒu�𒲐�
        JLabel moveLabel = new JLabel("�ړ��錾�}�X�F�i��FB2�j");
        moveLabel.setFont(new Font("Meiryo", Font.BOLD, 16));
        moveLabel.setBounds(850, 700, 300, 30);  // Y���W��700�ɕύX
        parent.add(moveLabel);

        moveInput = new JTextField();  // �ړ��R�}���h���͗p�̃e�L�X�g�t�B�[���h
        moveInput.setFont(new Font("Meiryo", Font.PLAIN, 14));
        sendButton = new JButton("�ړ�");  // �ړ����s�{�^��
        sendButton.setFont(new Font("Meiryo", Font.PLAIN, 14));
        moveTimer = new javax.swing.Timer(2000, null);  // �ړ�����p��2�b�^�C�}�[
        moveTimer.setRepeats(false);  // �^�C�}�[��1�񂾂����s

        // �ړ��{�^���̃A�N�V�������X�i�[��ݒ�
        // �ړ������̒��j�ƂȂ镔��
        sendButton.addActionListener(e -> { 
            System.out.println("=== �ړ������J�n ===");
            System.out.println("���݂̏��:");
            System.out.println("�v���C���[�ʒu: (" + playerX + "," + playerY + ")");
            System.out.println("�^�[��: " + (isMyTurn ? "����" : "����"));
            System.out.println("�Q�[�����: " + (gameEnded ? "�I��" : "�i�s��"));

            if(gameEnded) {
                System.out.println("�Q�[���I���ς�");
                JOptionPane.showMessageDialog(this, "�Q�[���͏I�����Ă��܂��B");
                return;
            }

            if(!isMyTurn) {
                System.out.println("����̃^�[��");
                JOptionPane.showMessageDialog(this, "����̃^�[���ł��B���΂炭���҂����������B");
                disableMovement();
                return;
            }

            String move = moveInput.getText().trim().toUpperCase();
            System.out.println("���͂��ꂽ�ړ�: " + move);

            if(!isValidMove(move)) {
                System.out.println("�s���Ȉړ��`��");
                JOptionPane.showMessageDialog(this, "�������`���œ��͂��Ă��������i��FB2�j");
                return;
            }

            int newRow = move.charAt(0) - 'A';
            int newCol = move.charAt(1) - '1';
            //System.out.println("�ړ�����W: (" + newRow + "," + newCol + ")");
            //System.out.println("�O���b�h�ړ���: (" + (newRow * 2) + "," + (newCol * 2) + ")");

            if(!isValidMovement(newRow, newCol)) {
                System.out.println("�����Ȉړ�: ��אڂ܂��͓����ʒu");
                JOptionPane.showMessageDialog(this, "���̃}�X�ɂ͈ړ��ł��܂���B\n�אڂ���}�X�ɂ݈̂ړ��\�ł��B");
                return;
            }

            //System.out.println("�ړ��O�̏��:");
            //System.out.println("opponentGrid[" + (newRow * 2) + "][" + (newCol * 2) + "] = " + opponentGrid[newRow * 2][newCol * 2]);

            // �ړ������J�n�O�Ƀ^�[�����I�����A���͂𖳌���
            isMyTurn = false;
            disableMovement();
            System.out.println("Turn disabled, waiting for wall check");
            
            // �������̈ړ��錾�\��
            addChatMessage("�V�X�e���@>> [ " + myName + " ] �� " + move + " �ֈړ��F���蒆...");
            // ���葤�ւ̈ړ��錾�ʒm
            out.println("CHAT MOVEMENT �V�X�e���F[ " + myName + " ] �� " + move + " �ֈړ����悤�Ƃ��Ă��܂�");
            out.flush();

            // �ړ�����p�̃^�C�}�[�����Z�b�g
            if (moveTimer != null) {
                moveTimer.stop();
            }
            
            // �ϐ���final�Ƃ��Đ錾
            final String finalMove = move;
            final int finalNewRow = newRow;
            final int finalNewCol = newCol;
            
            //�����2�b������
            moveTimer = new javax.swing.Timer(2000, event -> {
                //System.out.println("=== �ړ�����J�n ===");
                boolean hitWall = checkWall(finalNewRow, finalNewCol);
                
                if (!hitWall) {
                    // �ړ�����
                    addChatMessage("�V�X�e���@>> [ " + myName + " ] �� " + finalMove + " �ֈړ��Fyes, �i�߂܂�");
                    out.println("MOVE " + myName + " " + finalMove);
                    out.flush();
                    updatePlayerPosition(finalNewRow, finalNewCol);
                    moveInput.setText("");

                    // �S�[������
                    if(opponentGrid[finalNewRow * 2][finalNewCol * 2] == 3) {
                        System.out.println("�S�[�����B");
                        gameEnded = true;
                        JOptionPane.showMessageDialog(this, "�S�[���ɓ��B���܂����I���߂łƂ��������܂��I");
                        addChatMessage("�V�X�e��: [ " + myName + " ] ���S�[���ɓ��B���A�������܂����I");
                        out.println("GOAL " + myName);
                        out.flush();
                        disableMovement();
                    } else {
                        // �ړ��p�� - �ǂɓ������Ă��Ȃ��̂œ����v���C���[�̃^�[��
                        System.out.println("�ړ��p�� - �����v���C���[�̃^�[��");
                        isMyTurn = true;
                        enableMovement();
                        addChatMessage("�ǂɓ������Ă��Ȃ����߁A�����Ă��Ȃ��̔Ԃł�");
                    }
                } else {
                    // �ǂɏՓ�
                    System.out.println("�ǂɏՓ�: " + finalMove);
                    addChatMessage("�V�X�e���@>> [ " + myName + " ] �� " + finalMove + " �ֈړ��Fno, �ǂł�");
                    
                    // �T�[�o�[�ɒʒm
                    out.println("WALL_HIT " + myName);
                    out.flush();
                    addChatMessage("�ǂɓ�����܂����B���͑���̔Ԃł�");
                    
                    // �^�[���I��
                    disableMovement();
                }
                System.out.println("=== �ړ�����I�� ===");
            });
            moveTimer.setRepeats(false); // �^�C�}�[��1�񂾂����s
            moveTimer.start();
        });

        JPanel movePanel = new JPanel(new BorderLayout());
        movePanel.setBounds(850, 740, 300, 30);  // Y���W��740�ɕύX
        movePanel.add(moveInput, BorderLayout.CENTER);
        movePanel.add(sendButton, BorderLayout.EAST);
        parent.add(movePanel);
    }

    // ���͂��ꂽ�ړ��R�}���h�̌`������������������
    // �������`��: �A���t�@�x�b�g[A-F]�Ɛ���[1-6]�̑g�ݍ��킹
    private boolean isValidMove(String move) {
        return move.matches("^[A-F][1-6]$");  // ���K�\���Ńt�H�[�}�b�g���`�F�b�N
    }

    // �ړ��悪�L���Ȉʒu���ǂ����𔻒�
    // �אڂ���}�X�ւ̈ړ��݂̂������A�����ꏊ��΂߂ւ̈ړ����֎~
    private boolean isValidMovement(int newRow, int newCol) {
        // ����ړ����i�v���C���[�����z�u�j�͓����i0,0�j�̂݋���
        if(playerX == -1 || playerY == -1) {
            return newRow == 0 && newCol == 0;
        }
        
        // ���݈ʒu�Ɠ����ꏊ�ւ̈ړ����֎~
        if (newRow == playerX && newCol == playerY) {
            return false;
        }

        // ���O�̈ړ��ʒu�ւ̈ړ����֎~�i�s�����藈�����h�~�j
        if (!moveHistory.isEmpty()) {
            Point lastMove = moveHistory.get(moveHistory.size() - 1);
            if (lastMove.x == newCol * 110 + 60 && lastMove.y == newRow * 110 + 60) {
                return false;
            }
        }

        // �אڃ}�X�ւ̈ړ��̂݋���
        return Math.abs(newRow - playerX) + Math.abs(newCol - playerY) == 1;
    }

    // �ړ����͂�L����
    // �v���C���[�̃^�[���J�n���ɌĂяo�����
    private void enableMovement() {
        moveInput.setEnabled(true);  // ���̓t�B�[���h��L����
        sendButton.setEnabled(true);  // �ړ��{�^����L����
        moveInput.requestFocus();    // ���̓t�B�[���h�Ƀt�H�[�J�X��ݒ�
    }

    // �ړ����͂𖳌���
    // ����̃^�[���┻�蒆�ɌĂяo�����
    private void disableMovement() {
        moveInput.setEnabled(false);   // ���̓t�B�[���h�𖳌���
        sendButton.setEnabled(false);  // �ړ��{�^���𖳌���
        moveInput.setText("");         // ���̓t�B�[���h���N���A
    }

    // �`���b�g�G���A�Ƀ��b�Z�[�W��ǉ�
    public void addChatMessage(String message) {
        chatArea.append(message + "\n");  // ���b�Z�[�W��ǉ����ĉ��s
        // �ŐV�̃��b�Z�[�W��������悤�ɃX�N���[��
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    // �R���|�[�l���g�̕`�揈��
	/*
    // �ړ������̋O�Ղ�`�悷��(����������Ȃ�����)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);  // �O�Ղ̐F��Ԃɐݒ�
        g2d.setStroke(new BasicStroke(3));  // ���̑�����3�s�N�Z���ɐݒ�

        // �ړ������̐���`��
        if (moveHistory.size() > 1) {
            for (int i = 0; i < moveHistory.size() - 1; i++) {
                Point p1 = moveHistory.get(i);     // �ړ����̍��W
                Point p2 = moveHistory.get(i + 1); // �ړ���̍��W
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);  // 2�_�Ԃɐ�������
            }
        }
    }
	*/
	
    public void updateOpponentPosition(int row, int col) {
        // ���W�͈̔̓`�F�b�N
        if (row < 0 || row >= 6 || col < 0 || col >= 6) {
            System.err.println("�x��: �s���ȍ��W���w�肳��܂��� - row: " + row + ", col: " + col);
            return;
        }

        if (opponentX != -1 && opponentY != -1 && opponentX < 6 && opponentY < 6) {
            leftRoom[opponentX][opponentY].setIcon(null);
        }
        opponentX = row;
        opponentY = col;
        leftRoom[row][col].setIcon(opponentIcon);
        leftRoom[row][col].setDisabledIcon(opponentIcon); // ��������Ԃł������A�C�R����\��

        if(row == 5 && col == 5) {
            gameEnded = true;
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "���肪�S�[���ɓ��B���܂����B", "�s�k...", JOptionPane.INFORMATION_MESSAGE);
                addChatMessage("�V�X�e��: ����v���C���[���S�[���ɓ��B���܂����B���Ȃ��̕����ł��B");
                disableMovement();
            });
        } else if (!gameEnded) {
            // ���̃��b�Z�[�W�͎󓮓I�Ȃ��̂Ȃ̂ŁA���ۂ̃^�[���ύX��WALL_HIT���b�Z�[�W�ŏ���
            addChatMessage("����̈ړ����������܂���");
            isMyTurn = false;  // ����̈ړ������������̂ŁA�܂�����̃^�[��
            disableMovement(); // ���͂𖳌���
        }
    }

    public void updatePlayerPosition(int row, int col) {
        if (playerX != -1 && playerY != -1) {
            rightRoom[playerX][playerY].setIcon(null);
        }
        playerX = row;
        playerY = col;
        rightRoom[row][col].setIcon(playerIcon);
        rightRoom[row][col].setDisabledIcon(playerIcon); // ��������Ԃł������A�C�R����\��
    }
}

    // ���b�Z�[�W��M�p�̃X���b�h�N���X
    private class MesgRecvThread extends Thread {
        Socket socket;
        String myName;

        public MesgRecvThread(Socket s, String n) {
            socket = s;
            myName = n;
        }

        public void run() {
            try {
                InputStreamReader sisr = new InputStreamReader(socket.getInputStream());
                BufferedReader br = new BufferedReader(sisr);
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println(myName);

                while(true) {
                    String inputLine = br.readLine();
                    if (inputLine != null) {
                        System.out.println("��M: " + inputLine);
                        String[] inputTokens = inputLine.split(" ");
                        String cmd = inputTokens[0];

                        if(cmd.equals("CHAT")) {
                            String sender = inputTokens[1];
                            if (!sender.equals(myName)) {  // �����̃��b�Z�[�W�͕\�����Ȃ�
                                final String message = String.join(" ", Arrays.copyOfRange(inputTokens, 2, inputTokens.length));
                                SwingUtilities.invokeLater(() -> {
                                    if(gameScreen != null) {
                                        if (message.startsWith("MOVEMENT �V�X�e���F")) {
                                            String formattedMessage = message.replace("MOVEMENT �V�X�e���F", "����: ");
                                            gameScreen.addChatMessage(formattedMessage);
                                        } else if (!message.startsWith("�V�X�e��")) {
                                            gameScreen.addChatMessage("����: " + message);
                                        }
                                    }
                                });
                            }
                        } else if(cmd.equals("READY")) {
                            String[] messageParts = inputLine.split(" ", 2);
                            if (messageParts.length < 2) {
                                System.out.println("������READY���b�Z�[�W");
                                continue;
                            }
                            String sender = messageParts[1];
                            
                            if (!sender.equals(myName)) {
                                System.out.println("����̏�������");
                                System.out.println("����̑O�ɏ�������: " + opponentReady + ", isReady: " + isReady);
                                
                                if (!opponentReady) {
                                    opponentReady = true;
                                    System.out.println("����̌�ɏ�������: " + opponentReady);
                                    
                                    if (isReady && gameScreen == null) {
                                        SwingUtilities.invokeLater(() -> {
                                            System.out.println("���v���C���[�����������D�Q�[���X�^�[�g");
                                            startGame();
                                        });
                                    } else {
                                        System.out.println("����������҂��Ă���");
                                    }
                                } else {
                                    System.out.println("����͂��łɏ�������");
                                }
                            } else {
                                System.out.println("���g��READY���b�Z�[�W����");
                            }
                        } else if(cmd.equals("MOVE")) {
                            String sender = inputTokens[1];
                            if (!sender.equals(myName)) {  // �����̈ړ��͖���
                                if (inputTokens.length < 3) {
                                    System.err.println("�x��: �s����MOVE���b�Z�[�W�`���ł�");
                                    continue;
                                }
                                String position = inputTokens[2];
                                if (!position.matches("^[A-F][1-6]$")) {
                                    System.err.println("�x��: �s���Ȉړ��ʒu�`���ł�: " + position);
                                    continue;
                                }
                                final int row = position.charAt(0) - 'A';
                                final int col = position.charAt(1) - '1';
                                
                                // ���W�͈̔̓`�F�b�N
                                if (row < 0 || row >= 6 || col < 0 || col >= 6) {
                                    System.err.println("�x��: ���W���͈͊O�ł� - row: " + row + ", col: " + col);
                                    continue;
                                }

                                SwingUtilities.invokeLater(() -> {
                                    if(gameScreen != null) {
                                        gameScreen.updateOpponentPosition(row, col);
                                        gameScreen.addChatMessage("���肪 " + position + " �Ɉړ����܂���");
                                    }
                                });
                            }
                        } else if(cmd.equals("TURN_ORDER")) {
                            String turnPlayer = inputTokens[1];
                            boolean isFirst = Boolean.parseBoolean(inputTokens[2]);
                            
                            SwingUtilities.invokeLater(() -> {
                                if(gameScreen != null) {
                                    if (turnPlayer.equals(myName)) {
                                        gameScreen.isMyTurn = isFirst;
                                        if (isFirst) {
                                            gameScreen.addChatMessage("�V�X�e��: ���Ȃ���Player1�i��s�j�ł�");
                                            gameScreen.addChatMessage("�V�X�e��: Player1����Q�[�����J�n���Ă�������");
                                        } else {
                                            gameScreen.addChatMessage("�V�X�e��: ���Ȃ���Player2�i��U�j�ł�");
                                            gameScreen.addChatMessage("�V�X�e��: Player1����Q�[�����J�n���܂�");
                                        }
                                        
                                        // ��U�̏ꍇ�͓��͂𖳌���
                                        if (!isFirst) {
                                            gameScreen.moveInput.setEnabled(false);
                                            gameScreen.sendButton.setEnabled(false);
                                        }
                                    }
                                }
                            });
                        } else if(cmd.equals("MAZE_INFO")) {
                            String sender = inputTokens[1];
                            if (!sender.equals(myName)) {  // �����̖��H���͖���
                                // �ꎞ�ۑ��p�̔z����N���A
                                tempOpponentWalls.clear();
                                tempOpponentGrid = new int[11][11];
                                
                                // �O���b�h�̏�����
                                for(int i = 0; i < 11; i++) {
                                    for(int j = 0; j < 11; j++) {
                                        if (i%2!=0 && j%2!=0) {
                                            tempOpponentGrid[i][j] = 1;
                                        } else {
                                            tempOpponentGrid[i][j] = 0;
                                        }
                                    }
                                }

                                // �����A�o���A�ǂ̏��𒲂ׂ�
                                for(int i = 2; i < inputTokens.length; i++) {
                                    String info = inputTokens[i];
                                    if(info.startsWith("E")) { // ����
                                        int pos = Integer.parseInt(info.substring(1));
                                        int row = pos / 6;
                                        int col = pos % 6;
                                        tempOpponentGrid[row*2][col*2] = 2;
                                    } else if(info.startsWith("X")) { // �o��
                                        int pos = Integer.parseInt(info.substring(1));
                                        int row = pos / 6;
                                        int col = pos % 6;
                                        tempOpponentGrid[row*2][col*2] = 3;
                                    } else if(info.startsWith("W")) { // ��
                                        String[] coords = info.substring(1).split(",");
                                        int wallX = Integer.parseInt(coords[0]);
                                        int wallY = Integer.parseInt(coords[1]);
                                        tempOpponentWalls.add(new int[]{wallX, wallY});
                                        
                                        // �O���b�h�ɕǂ�ݒ�
                                        if (wallX >= 100 && wallX < 200) { // �����̕�
                                            int row = (wallX - 100) / 5;
                                            int col = (wallX - 100) % 5;
                                            tempOpponentGrid[row*2][col*2+1] = 1;
                                        } else if (wallX >= 200) { // �����̕�
                                            int row = (wallX - 200) / 6;
                                            int col = (wallX - 200) % 6;
                                            tempOpponentGrid[row*2+1][col*2] = 1;
                                        }
                                    }
                                }

                                // ��񂪈ꎞ�ۑ����ꂽ���Ƃ������t���O��ݒ�
                                hasTempMazeInfo = true;

                                // �������łɃQ�[����ʂ�����������Ă���΁A���𒼐ڐݒ�i�G���[�悭�o��j
                                if (gameScreen != null) {
                                    SwingUtilities.invokeLater(() -> {
                                        gameScreen.opponentWalls.clear();
                                        gameScreen.opponentWalls.addAll(tempOpponentWalls);
                                        gameScreen.opponentGrid = tempOpponentGrid.clone();
                                    });
                                }
                            }
                        } else if(cmd.equals("GOAL")) {
                            String goalSender = inputTokens[1];
                            if (!goalSender.equals(myName)) {  // ����̃S�[���ʒm����M
                                SwingUtilities.invokeLater(() -> {
                                    if(gameScreen != null) {
                                        gameScreen.gameEnded = true;
                                        JOptionPane.showMessageDialog(null, "����v���C���[���S�[���ɓ��B���܂����B", "�Q�[���I��", JOptionPane.INFORMATION_MESSAGE);
                                        gameScreen.addChatMessage("�V�X�e��: [ " + goalSender + "] ���S�[���ɓ��B���܂����B���Ȃ��̕����ł��B");
                                        gameScreen.disableMovement();
                                    }
                                });
                            }
                        } else if(cmd.equals("WALL_HIT")) {
                            String wallHitSender = inputTokens[1];
                            if (!wallHitSender.equals(myName)) {  // �����̕ǔ���͖���
                                SwingUtilities.invokeLater(() -> {
                                    if(gameScreen != null) {
                                        gameScreen.isMyTurn = true;  // ���肪�ǂɓ��������̂Ŏ����̃^�[��
                                        gameScreen.addChatMessage("���肪�ǂɓ�����܂����B���Ȃ��̔Ԃł�");
                                        gameScreen.enableMovement();  // ���͂�L����
                                    }
                                });
                            } else {
                                // �������ǂɓ��������ꍇ�̏����i�O�̂��߁j
                                if(gameScreen != null) {
                                    gameScreen.isMyTurn = false;  // �����I�ɑ���̃^�[����
                                    gameScreen.disableMovement();
                                }
                            }
                        }
                    } else {
                        break;
                    }
                }
                socket.close();
            } catch (IOException e) {
                System.err.println("�G���[���������܂���: " + e);
            }
        }
    }
}

