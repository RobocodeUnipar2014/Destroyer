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
 * @author Administrador
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
				gunTurnAmt = 50;
			}
			if (count > 20) {
				trackName = null;
			}
			
		}
	}

	@Override
	public void onHitByBullet(HitByBulletEvent event) {// quando o robo leva um
														// tiro
		//quando o robo é atingido, grava o nome do outro robo e atira.
		if (trackName != null && !trackName.equals(event.getName())) {
			out.println("Tracking " + event.getName() + " due to collision");
		}
		trackName = event.getName();
		gunTurnAmt = normalRelativeAngleDegrees(event.getBearing()
				+ (getHeading() - getRadarHeading()));
		turnGunRight(gunTurnAmt);
		fire(3);
		
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
			ahead(e.getDistance() - 140);
			return;
		}
		//vira a arma e atira no robo localizado
		gunTurnAmt = normalRelativeAngleDegrees(e.getBearing()
				+ (getHeading() - getRadarHeading()));
		turnGunRight(gunTurnAmt);
		fire(3);
		
		
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
		fire(3);
		ahead(50);
	}
 // qnd ganha, gira para os lados
	public void onWin(WinEvent e) {
		for (int i = 0; i < 50; i++) {
			turnRight(3600);
			turnLeft(3600);
		}
	}
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
		//reverte a direção qnd atingido
		reverseDirection();
	}
}
