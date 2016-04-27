///**
// * 
// */
//package core;
//
//import javafx.animation.AnimationTimer;
//
///**
// * @author danny
// * 
// *
// */
//public abstract class Ticker {
//
//	private AnimationTimer animationTimer;
//	private int passedTicks = 0;
//	private double lastNanoTime = System.nanoTime();
//	private double time = 0;
//	
//	private void initAnimationTimer() {
//		final double fps = 60.0;
//		animationTimer = new AnimationTimer() {
//			@Override
//			public void handle(long currentNanoTime) {
//				// calculate time since last update.
//				time += (currentNanoTime - lastNanoTime) / 1000000000.0;
//				lastNanoTime = currentNanoTime;
//				passedTicks = (int) Math.floor(time * fps);
//				time -= passedTicks / fps;
//
//				if (ticker != null) {
//					ticker.tick(passedTicks);
//				}
//			}
//		};
//	}
//	
//	public abstract void tick(int ticks);
//}
