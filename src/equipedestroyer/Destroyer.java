/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package equipedestroyer;

import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.Color;

import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;

/**
 * 
 * @author Equipe Destroyer 
 */
public class Destroyer extends Robot {

	int count = 0;
	boolean movingForward;
	double gunTurnAmt;
	String trackName;

	@Override
	public void run() {
		setBodyColor(Color.black);
		setGunColor(Color.red);
		setRadarColor(Color.white);
		setBulletColor(Color.red);
		

		trackName = null;

		while (true) {
			turnGunRight(gunTurnAmt);
			count++;
			if (count > 2) {
				gunTurnAmt = -10;
			}
			if (count > 5) {
				gunTurnAmt = 40;
			}
			if (count > 20) {
				trackName = null;
			}
			
		}
	}

	@Override
	public void onHitByBullet(HitByBulletEvent event) {// quando o robo leva um
														// tiro
		//quando o robo � atingido, grava o nome do outro robo e atira.
		if (trackName != null && !trackName.equals(event.getName())) {
			out.println("Tracking " + event.getName() + " due to collision");
		}
		trackName = event.getName();
		gunTurnAmt = normalRelativeAngleDegrees(event.getBearing()
				+ (getHeading() - getRadarHeading()));
		turnGunRight(gunTurnAmt);
		atirar(null, null, event);
		
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent e) {// quando encontra robo
														// oponente
		//aproxima do robo  
		if (e.getDistance() > 150) {
			gunTurnAmt = normalRelativeAngleDegrees(e.getBearing()
					+ (getHeading() - getRadarHeading()));

			turnGunRight(gunTurnAmt);
			turnRight(e.getBearing());
			ahead(e.getDistance() -140);
			return;
		}
		//vira a arma e atira no robo localizado
		gunTurnAmt = normalRelativeAngleDegrees(e.getBearing()
				+ (getHeading() - getRadarHeading()));
		turnGunRight(gunTurnAmt);
		atirar(e, null , null);
		
		
		if (e.getDistance() < 100) {
			if (e.getBearing() > -90 && e.getBearing() <= 90) {
				back(40);
			} else {
				ahead(40);
			}
		}
		scan();
	}


	
	//qnd bate em outro robo, se vira para ele atira e anda a frente
	public void onHitRobot(HitRobotEvent e) {
		if (trackName != null && !trackName.equals(e.getName())) {
			out.println("Tracking " + e.getName() + " due to collision");
		}
		trackName = e.getName();

		gunTurnAmt = normalRelativeAngleDegrees(e.getBearing()
				+ (getHeading() - getRadarHeading()));
		turnGunRight(gunTurnAmt);
		atirar(null, e, null);
		ahead(20);
	}
 // qnd ganha, gira para os lados
	public void onWin(WinEvent e) {
		for (int i = 0; i < 50; i++) {
			turnRight(3600);
			turnLeft(3600);
		}
	}
	
	//reverter a dire��o
	public void reverseDirection() {
		if (movingForward) {
			back(40000);
			movingForward = false;
		} else {
			ahead(40000);
			movingForward = true;
		}
	}
	
	public void onHitWall(HitWallEvent e) {
		//reverte a dire��o qnd bate na parede
		reverseDirection();
	}
	
	//atira com for�a diferente dependendo da vida do inimigo
	public void atirar(ScannedRobotEvent e, HitRobotEvent f, HitByBulletEvent h) {
		if(e.getEnergy()>80){
			fireBullet(3);
		}
		if(e.getEnergy()>50){
			fireBullet(2);
		}
		if(e.getEnergy()>20){
			fireBullet(1);
		}
	}
}

