/*
 * Copyright (C) 2011 4th Line GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.seamless.util.math;


public class Rectangle {

    private Point position;
    private int width;
    private int height;

    public Rectangle() {
    }

    public Rectangle(Point position, int width, int height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    public void reset() {
        position = new Point(0, 0);
        width = 0;
        height = 0;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Rectangle intersection(Rectangle that) {
        int tx1 = this.position.getX();
        int ty1 = this.position.getY();
        int rx1 = that.position.getX();
        int ry1 = that.position.getY();
        long tx2 = tx1;
        tx2 += this.width;
        long ty2 = ty1;
        ty2 += this.height;
        long rx2 = rx1;
        rx2 += that.width;
        long ry2 = ry1;
        ry2 += that.height;
        if (tx1 < rx1) tx1 = rx1;
        if (ty1 < ry1) ty1 = ry1;
        if (tx2 > rx2) tx2 = rx2;
        if (ty2 > ry2) ty2 = ry2;
        tx2 -= tx1;
        ty2 -= ty1;

        // tx2,ty2 will never overflow (they will never be
        // larger than the smallest of the two source w,h)
        // they might underflow, though...
        if (tx2 < Integer.MIN_VALUE) tx2 = Integer.MIN_VALUE;
        if (ty2 < Integer.MIN_VALUE) ty2 = Integer.MIN_VALUE;
        return new Rectangle(new Point(tx1, ty1), (int) tx2, (int) ty2);
    }

    public boolean isOverlapping(Rectangle that) {
        Rectangle intersection = this.intersection(that);
        return (intersection.getWidth() > 0 && intersection.getHeight() > 0);
    }

    @Override
    public String toString() {
        return "Rectangle(" + position + " - " + width + "x" + height+")";
    }


}
