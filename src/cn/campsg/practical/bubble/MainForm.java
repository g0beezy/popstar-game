package cn.campsg.practical.bubble;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import cn.campsg.practical.bubble.entity.MovedStar;
import cn.campsg.practical.bubble.entity.Star;
import cn.campsg.practical.bubble.entity.StarList;
import cn.campsg.practical.bubble.service.StarService;
import cn.campsg.practical.bubble.service.StarServiceImpl;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

/**
 * 泡泡糖窗体类，用于显示泡泡糖阵列、处理泡泡糖点击事件与动画
 * 
 * 
 * @author Frank.Chen
 * @version 1.5
 */
public class MainForm extends Application {
	
	/** 从服务端获取的完整10*10泡泡糖列表  **/
	private StarList mCurretStars = null;
	
	/** 窗体中显示泡泡糖的区域 **/
	private AnchorPane mStarForm = null;

	/** 正在播放的移动动画数量，>0 时禁止点击，防止动画期间数据错位 **/
	private int mAnimatingCount = 0;

	public static void show(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass()
					.getResource("/res/layout/main_layout.fxml"));

			// 将主布局加入到视图场景中
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);

			// 页面加载时或重新开始新的泡泡糖棋局时候初始化泡泡糖
			initGameStars(root);

			primaryStage.setTitle("消灭泡泡糖-Popstar3");
			primaryStage.setResizable(false);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 页面加载时或重新开始新的泡泡糖棋局时候初始化泡泡糖
	 * 
	 * @param root
	 *            窗体总布局
	 * 
	 */
	private void initGameStars(AnchorPane root) {

		// 返回窗体中显示泡泡糖的区域
		/** 环境包: 提供 **/
		mStarForm = (AnchorPane) root.lookup("#game_pane");

		// 创建消灭泡泡糖业务类
		/** 环境包: 不提供，指导手册要求学员完成 **/
		StarService starService =  new StarServiceImpl();
		//任务03完成以下代码

		// 创建调用创建泡泡糖代码
		/** 环境包: 不提供，指导手册要求学员完成 **/
		mCurretStars = starService.createStars();

		// 循环遍历所有泡泡糖，将泡泡糖对象Star转化为界面显示控件Label
		if(mCurretStars != null)
			/** 环境包: 不提供，指导手册要求学员完成 **/
			for (int i = 0; i < mCurretStars.size(); i++) {
				// 从泡泡糖集合中取出一个泡泡糖
				/** 环境包: 不提供，指导手册要求学员完成 **/
				Star star = mCurretStars.get(i);
	
				// 创建界面泡泡糖显示控件Label
				/** 环境包: 不提供，指导手册提供源代码并讲解代码含义 **/
				Label starFrame = new Label();
				starFrame.setPrefWidth(48);
				starFrame.setPrefHeight(48);
	
				// 获取泡泡糖对象Star的行与列
				/** 环境包: 不提供，指导手册要求学员完成 **/
				int row = star.getPosition().getRow();
				int col = star.getPosition().getColumn();
	
				// 为泡泡糖显示控件Label设置唯一标识ID，ID规则为s+行号+列号（例如：s00,s01）
				/** 环境包: 不提供，指导手册提供源代码并讲解代码含义 **/
				starFrame.setId("s" + row + col);
				// 将泡泡糖的坐标位置保存起来用于识别泡泡糖在界面中的位置。
				/** 环境包: 不提供，指导手册提供源代码并讲解代码含义 **/
				starFrame.setUserData(row + ";" + col);
				// 设置泡泡糖显示控件Label在界面的呈现坐标
				/** 环境包: 不提供，指导手册提供源代码并讲解代码含义 **/
				starFrame.setLayoutX(col * 48);
				starFrame.setLayoutY(row * 48);
	
				// 设置泡泡糖显示控件Label显示外观
				/** 环境包: 不提供，指导手册提供源代码并讲解代码含义 **/
				switch (star.getType().value()) {
				case 0:
					starFrame.getStyleClass().add("blue_star");
					break;
				case 1:
					starFrame.getStyleClass().add("green_star");
					break;
				case 2:
					starFrame.getStyleClass().add("yellow_star");
					break;
				case 3:
					starFrame.getStyleClass().add("red_star");
					break;
				case 4:
					starFrame.getStyleClass().add("purple_star");
					break;
				}
starFrame.setOnMouseClicked(event -> {
    // 动画播放期间禁止点击，防止界面与数据错位
    if (mAnimatingCount > 0) return;

    String data = (String) starFrame.getUserData();
    String[] parts = data.split(";");
    int r = Integer.parseInt(parts[0]);
    int c = Integer.parseInt(parts[1]);

    Star clicked = mCurretStars.getStar(r, c);
    if (clicked == null) return;

    StarServiceImpl service = new StarServiceImpl();          // ← 改这里
    StarList cleared = service.tobeClearedStars(clicked, mCurretStars);
    if (cleared.size() == 0) return;

    for (int j = 0; j < cleared.size(); j++) {                // ← i 改 j
        Star s = cleared.get(j);                              // ← i 改 j
        int sr = s.getPosition().getRow();
        int sc = s.getPosition().getColumn();
        mStarForm.getChildren().remove(mStarForm.lookup("#s" + sr + sc));
        mCurretStars.removeStar(sr, sc);
    }

    // 计算待垂直移动的泡泡糖并播放下落动画
    StarList movedStars = service.getYMovedStars(cleared, mCurretStars);
    mAnimatingCount = movedStars.size();
    for (int k = 0; k < movedStars.size(); k++) {
        MovedStar ms = (MovedStar) movedStars.get(k);
        int oldR = ms.getUnmovedPosition().getRow();
        int oldC = ms.getUnmovedPosition().getColumn();
        final int newR = ms.getPosition().getRow();
        final int newC = ms.getPosition().getColumn();

        // 更新数据模型中星星的位置
        Star starData = mCurretStars.getStar(oldR, oldC);
        if (starData != null) {
            starData.setPosition(ms.getPosition());
        }

        // 找到界面上对应的Label
        Label lbl = (Label) mStarForm.lookup("#s" + oldR + oldC);
        if (lbl == null) {
            mAnimatingCount--;
            continue;
        }
        // 动画开始前就更新ID和userData，与数据模型保持同步
        lbl.setId("s" + newR + newC);
        lbl.setUserData(newR + ";" + newC);

        // 播放平移动画
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), lbl);
        tt.setByY((newR - oldR) * 48);
        tt.setOnFinished(e -> {
            // 动画结束后落位：清掉位移量，更新真实坐标
            lbl.setTranslateY(0);
            lbl.setLayoutY(newR * 48);
            mAnimatingCount--;
        });
        tt.play();
    }
});
	
				// 将泡泡糖加入到窗体中显示泡泡糖的区域
				/** 环境包: 不提供，指导手册提供源代码并讲解代码含义 **/
				mStarForm.getChildren().add(starFrame);
			}

	}
}
