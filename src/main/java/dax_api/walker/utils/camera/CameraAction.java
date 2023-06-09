package dax_api.walker.utils.camera;


import dax_api.walker.utils.AccurateMouse;
import dax_api.walker.utils.movement.WalkingQueue;
import dax_api.walker_engine.WaitFor;
import net.runelite.rsb.methods.Web;
import net.runelite.rsb.util.StdRandom;
import net.runelite.rsb.wrappers.RSCharacter;
import net.runelite.rsb.wrappers.common.Positionable;
import net.runelite.rsb.wrappers.RSTile;

import java.awt.*;

public class CameraAction {

    private static final Rectangle HOVER_BOX = new Rectangle(140, 20, 260, 110);

    public static void moveCamera(Positionable destination){
        if (isMiddleMouseCameraOn()){
            DaxCamera.focus(destination);
        } else {
            AsynchronousCamera.focus(destination);
        }
    }

    public static boolean focusCamera(Positionable positionable){
        RSTile tile = positionable.getLocation();
        if (tile.isOnScreen() && tile.isClickable()){
            return true;
        }

        if (isMiddleMouseCameraOn()){
            DaxCamera.focus(tile);
            return tile.isOnScreen() && tile.isClickable();
        } else {
            AsynchronousCamera.focus(tile);
            Point currentMousePosition = new Point (Web.methods.mouse.getLocation().getX(), Web.methods.mouse.getLocation().getY());
            if (!HOVER_BOX.contains(currentMousePosition)) {
                Web.methods.mouse.move(AccurateMouse.getRandomPoint(HOVER_BOX));
            }
            return WaitFor.condition(StdRandom.uniform(3000, 5000), () -> tile.isOnScreen() && tile.isClickable() ? WaitFor.Return.SUCCESS : WaitFor.Return.IGNORE) == WaitFor.Return.SUCCESS;
        }
    }

    public static boolean focusCamera(RSCharacter rsCharacter){
        if (rsCharacter.isOnScreen() && rsCharacter.isClickable()){
            return true;
        }

        RSTile destination = new RSTile(rsCharacter.getLocation());
        RSTile newDestination = WalkingQueue.getWalkingTowards(rsCharacter);
        if (newDestination != null){
            destination = newDestination;
        }

        if (isMiddleMouseCameraOn()){
            DaxCamera.focus(destination);
            return rsCharacter.isOnScreen() && rsCharacter.isClickable();
        } else {
            AsynchronousCamera.focus(destination);
            Point currentMousePosition = new Point (Web.methods.mouse.getLocation().getX(), Web.methods.mouse.getLocation().getY());
            if (!HOVER_BOX.contains(currentMousePosition)) {
                if (!HOVER_BOX.contains(currentMousePosition)) {
                    Web.methods.mouse.move(AccurateMouse.getRandomPoint(HOVER_BOX));
                }
            }
            return WaitFor.condition(StdRandom.uniform(3000, 5000), () -> rsCharacter.isOnScreen() && rsCharacter.isClickable() ? WaitFor.Return.SUCCESS : WaitFor.Return.IGNORE) == WaitFor.Return.SUCCESS;
        }
    }


    public static boolean isMiddleMouseCameraOn() {
        return Web.methods.client.getVarbitValue(4134) == 0;
    }

}
