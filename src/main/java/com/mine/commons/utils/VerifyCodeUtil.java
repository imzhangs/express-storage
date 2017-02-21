package com.mine.commons.utils;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class VerifyCodeUtil
{
    public static final String CHECK_CODE_KEY = "check_code";
    public static final int WIDTH = 96;
    public static final int HEIGHT = 28;
    
    public static Color codeFontColors[]=new Color[]{Color.BLACK,Color.BLUE};
    public static Color backgroudColors[]=new Color[]{
		new Color(0xF5F5F5),new Color(0xEEE8AA),
		new Color(0xF0FFF0),new Color(0xFFFFF0),
		new Color(0xFFEFD5),new Color(0xFFF5EE),
    };
    
    /**
     * <p>取得验证码图片，并把生成的数字放到HttpSession中</p>
     * @return
     * 
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static BufferedImage getVerifyImage(char[] code){
        //创建内存图象并获得其图形上下文
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        //产生图像
        drawBackground(g);
        drawRands(g, code);
        g.dispose();
        return image;
    }
    
    /**
     * <p>随机产生验证码字符</p>
     * @return
     * 
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static char[] generateCheckCode(int length)
    {
        //定义验证码的字符表
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        if(length<4){
        	length=4;
        }
        char[] rands = new char[length];
        for (int i = 0; i < length; i++)
        {
            int rand = (int) (Math.random() * chars.length());
            rands[i] = chars.charAt(rand);
        }
        return rands;
    }

    /**
     * <p>字符位置、字体属性</p>
     * @param g
     * @param rands
     * 
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static void drawRands(Graphics g, char[] rands)
    {
        g.setColor(codeFontColors[new Random().nextInt(codeFontColors.length)]);
        g.setFont(new Font(null, Font.ITALIC  , 28));
        //在不同的高度上输出验证码的每个字符 
        g.drawString("" + rands[0], new Random().nextInt(5)+10, new Random().nextInt(12)+15);
        g.drawString("" + rands[1], new Random().nextInt(10)+25, new Random().nextInt(12)+15);
        g.drawString("" + rands[2], new Random().nextInt(15)+45, new Random().nextInt(12)+15);
        g.drawString("" + rands[3], new Random().nextInt(20)+55, new Random().nextInt(12)+15);
        //System.out.println(rands);
    }

    /**
     * <p>背景</p>
     * @param g
     * 
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static void drawBackground(Graphics g)
    {
        //画背景
        g.setColor(backgroudColors[new Random().nextInt(backgroudColors.length)]);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        //随机产生120个干扰点
        for (int i = 0; i < 120; i++)
        {
            int x = (int) (Math.random() * WIDTH);
            int y = (int) (Math.random() * HEIGHT);
            int red = (int) (Math.random() * 255);
            int green = (int) (Math.random() * 255);
            int blue = (int) (Math.random() * 255);
            g.setColor(new Color(red, green, blue));
            g.drawOval(x, y, new Random().nextInt(5)+6, 0);
        }
        //画4条直线
        for (int i = 0; i < 4; i++)
        {
            int red = (int) (Math.random() * 255);
            int green = (int) (Math.random() * 255);
            int blue = (int) (Math.random() * 255);
            g.setColor(new Color(red, green, blue));
            int x1=(int) (Math.random() * WIDTH);
            int x2=(int) (Math.random() * HEIGHT);
            int y1=(int) (Math.random() * WIDTH);
            int y2=(int) (Math.random() * HEIGHT);
            g.drawLine(x1+1,x2+1 ,y1+1 , y2+1);
            g.drawLine(x1,x2 ,y1 , y2);
            g.drawLine(x1-1,x2-1 ,y1-1 , y2-1);
            g.drawLine(x1-2,x2-2 ,y1-2, y2-3);
            g.drawLine(x1-3,x2-3 ,y1-3 , y2-2);
            g.drawLine(x1-4,x2-4 ,y1-4 , y2-4);
            g.drawLine(x1-5,x2-5 ,y1-5 , y2-5);
            g.drawLine(x1-6,x2-6 ,y1-6 , y2-6);
        }
    }
}