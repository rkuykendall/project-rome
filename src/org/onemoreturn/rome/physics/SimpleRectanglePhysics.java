package org.onemoreturn.rome.physics;

import android.graphics.RectF;

import org.onemoreturn.rome.graphics.Rectangle;
import org.onemoreturn.rome.graphics.Shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleRectanglePhysics implements IPhysics2D {

	private List<Rectangle> mPossibleCollisions;
	private boolean mIntersects = false;

	public SimpleRectanglePhysics(Rectangle... rects) {
		this(Arrays.asList(rects));
	}

	public SimpleRectanglePhysics(List<Rectangle> rects) {
		mPossibleCollisions = rects;
	}

	public boolean addRect(Rectangle rect) {
		if (mPossibleCollisions == null)
			mPossibleCollisions = new ArrayList<>();

		return mPossibleCollisions.add(rect);
	}

	public boolean removeRect(Rectangle rect) {
		if (mPossibleCollisions == null)
			return false;

		return mPossibleCollisions.remove(rect);
	}

	@Override
	public void applyPhysics(Shape shape) {
		Rectangle rect = (Rectangle) shape;
		rect.update();

		mIntersects = false;

		for (Rectangle possibleCollision : mPossibleCollisions) {
			if (rect.intersects(possibleCollision)) {
				mIntersects = true;
				rect.translate(-rect.speed().x, -rect.speed().y);

				RectF boundsRect = rect.bounds();
				RectF boundsCollision = possibleCollision.bounds();

				if (boundsRect.bottom <= boundsCollision.top) {
					rect.translate(rect.speed().x, 0.0f);
					rect.setSpeed(rect.speed().x, 0.0f);
					rect.setGrounded(true);
				} else if (boundsRect.left >= boundsCollision.right) {
					rect.translate(0.0f, rect.speed().y);
					rect.setSpeed(0.0f, rect.speed().y);
				} else if (boundsRect.right <= boundsCollision.left) {
					rect.translate(0.0f, rect.speed().y);
					rect.setSpeed(0.0f, rect.speed().y);
				} else if (boundsRect.top >= boundsCollision.bottom) {
					rect.translate(rect.speed().x, 0.0f);
					rect.setSpeed(rect.speed().x, -0.01f);
				}
			}
		}

		if (!mIntersects) {
			float y = rect.speed().y;
			y -= 0.001f;
			if (y <= -0.02f) {
				y = -0.02f;
			}
			rect.setSpeed(rect.speed().x, y);
		}

	}
}
