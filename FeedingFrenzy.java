import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library

import java.util.Random;
import java.awt.*;
// and predefined colors (Red, Green, Yellow, Blue, Black, White)

// a type of fish
interface IFish {
  // checks if one fish can eat the other fish
  boolean eatFish(AFish other);

  // adds the radii of two fish
  int addRadius(AFish fsh1);
}

// common features of fish
abstract class AFish implements IFish {
  int radius;
  Color clr;
  int x;
  int y;

  public AFish(int radius, Color clr, int x, int y) {
    this.radius = radius;
    this.clr = clr;
    this.x = x;
    this.y = y;
  }
  /* TEMPLATE:
     fields:
      this.radius ... int
      this.clr ... Color
      this.x ... int
      this.y ... int
     methods:
      this.eatFish(AFish) ... boolean
      this.distanceFormula(AFish) ... double
      this.addRadius(AFish) ... int
      this.draw(WorldScene) ... WorldScene
      this.drawFish() ... CircleImage
      this.biggestFish(AFish) ... boolean
   */

  // determines if the given fish can eat another fish
  public boolean eatFish(AFish other) {
    return this.radius >= other.radius;
  }

  // calculates the distance between two fish using distance formula
  public double distanceFormula(AFish fsh1) {
    return Math.pow(Math.pow(this.x - fsh1.x, 2) + Math.pow(this.y - fsh1.y, 2), 0.5);
  }

  // adds the radii of two fish
  public int addRadius(AFish fsh1) {
    return this.radius + fsh1.radius;
  }


  // draws the world scene
  public WorldScene draw(WorldScene acc) {
    return acc.placeImageXY(new RectangleImage(2, 1000, "solid", Color.BLACK), 1001, 500)
            .placeImageXY(this.drawFish(), this.x, this.y);
  }

  // draws a circle based on the fish constructor
  public CircleImage drawFish() {
    return new CircleImage(this.radius, "solid", this.clr);
  }

  // checks if a fish is bigger than another fish
  boolean biggestFish(AFish other) {
    return this.radius < other.radius;
  }

}

// represents the main fish being controlled by the user
class PlayerFish extends AFish {

  int lives;
  int score;
  int speed;
  double velocityX;
  double velocityY;
  boolean inertia;

  public PlayerFish(int radius, Color clr, int x, int y, int lives, int score, int speed,
                    double velocityX, double velocityY, boolean inertia) {
    super(radius, clr, x, y);
    this.lives = lives; // lives should always start at three
    this.score = score; // score should always start at zero
    this.speed = speed; // speed should always begin at 1
    this.velocityX = velocityX; // velocity should always start at zero
    this.velocityY = velocityY; // velocity should always start at zero
    this.inertia = inertia; //should start at false
  }
  /* TEMPLATE:
     fields:
      this.size ... double
      this.radius ... int
      this.clr ... Color
      this.x ... int
      this.y ... int
      this.lives ... int
      this.score ... int
      this.speed ... boolean
      this.velocityX ... double
      this.velocityY ... double
      this.inertia ... boolean
     methods:
      this.moveX(double) ... PlayerFish
      this.moveY(double) ... PlayerFish
      this.multiply1(double) ... int
      this.setZero() ... PlayerFish
      this.lostLife() ... PlayerFish
      this.noLives() ... boolean
      this.accel() ... double
      this.inertiaOn() ... PlayerFish
      this.slowDown() ... PlayerFish
      this.getBigger(BkgdFish) ... PlayerFish
      this.upSpeed() ... PlayerFish
      this.doubleSize() ... PlayerFish
      this.draw(WorldScene) ... WorldScene
   */

  // moves the fish in the X direction & ensures it does not go beyond the border
  PlayerFish moveX(double num) {
    if (this.x > 997) {
      return new PlayerFish(this.radius, this.clr, 10, this.y, this.lives, this.score, this.speed,
              this.velocityX, this.velocityY, this.inertia);
    } else if (this.x < 3) {
      return new PlayerFish(this.radius, this.clr, 990, this.y, this.lives, this.score,
              this.speed, this.velocityX, this.velocityY, this.inertia);
    } else if ((int)(Math.abs(this.velocityX + num)) >= 8.0 * this.speed) {
      return new PlayerFish(this.radius, this.clr, this.x + 8 * multiply1(num) * this.speed, this.y,
              this.lives, this.score, this.speed, 8.0 * multiply1(num) * this.speed, this.velocityY,
              this.inertia);
    } else {
      return new PlayerFish(this.radius, this.clr,
              (int) (this.velocityX + num) + this.x, this.y, this.lives,
              this.score, this.speed, this.velocityX + num, this.velocityY, this.inertia);
    }
  }

  // moves the fish in the Y direction & ensures it does not go beyond the border
  PlayerFish moveY(double num) {
    if (this.y > 997) {
      return new PlayerFish(this.radius, this.clr, this.x, 10, this.lives, this.score, this.speed,
              this.velocityX, this.velocityY, this.inertia);
    } else if (this.y < 3) {
      return new PlayerFish(this.radius, this.clr, this.x, 990, this.lives, this.score,
              this.speed, this.velocityX, this.velocityY, this.inertia);
    } else if ((int) Math.abs(this.velocityY + num) >= 8.0 * this.speed) {
      return new PlayerFish(this.radius, this.clr, this.x, this.y + 8 * multiply1(num) * this.speed,
              this.lives, this.score, this.speed, this.velocityX, 8.0 * multiply1(num) * this.speed,
              this.inertia);
    } else {
      return new PlayerFish(this.radius, this.clr, this.x,
              (int) (this.velocityY + num ) + this.y, this.lives,
              this.score, this.speed, this.velocityX, this.velocityY + num, this.inertia);
    }
  }

  // helper function that is meant to make sure the correct signs are being use
  int multiply1(double num) {
    if (num < 0) {
      return -1;
    } else {
      return 1;
    }
  }

  // helper function for onKey that stops all velocity and turns off inertia
  public PlayerFish setZero() {
    if (this.inertia) {
      return new PlayerFish(this.radius, this.clr, this.x, this.y, this.lives, this.score,
              this.speed, 0.0, 0.0, false);
    } else {
      return this;
    }
  }

  // reduces life count by 1
  public PlayerFish lostLife() {
    return new PlayerFish(this.radius, this.clr, this.x, this.y, this.lives - 1, this.score,
            this.speed, 0, 0, this.inertia);
  }

  // checks if the fish has any lives left
  public boolean noLives() {
    return this.lives == 0;
  }

  // bases the acceleration
  public double accel() {
    if (this.radius < 100) {
      return Math.round((10.0 / this.radius % 10) * 100.0) / 100.0;
    } else if (this.radius < 200) {
      return Math.round((20.0 / this.radius % 10) * 100.0) / 100.0;
    } else {
      return Math.round((30.0 / this.radius % 10) * 100.0) / 100.0;
    }
  }

  // turns the inertia for the fish on
  public PlayerFish inertiaOn() {
    return new PlayerFish(this.radius, this.clr, this.x, this.y, this.lives, this.score,
            this.speed, this.velocityX, this.velocityY, true);
  }

  // handles all movement for inertia
  public PlayerFish slowDown() {
    if (this.inertia && this.x > 997) {
      return new PlayerFish(this.radius, this.clr, 10, this.y, this.lives, this.score, this.speed,
              this.velocityX, this.velocityY, this.inertia);
    } else if (this.inertia && this.x < 3) {
      return new PlayerFish(this.radius, this.clr, 990, this.y, this.lives, this.score, this.speed,
              this.velocityX, this.velocityY, this.inertia);
    } else if (this.inertia && this.y > 997) {
      return new PlayerFish(this.radius, this.clr, this.x, 10, this.lives, this.score, this.speed,
              this.velocityX, this.velocityY, this.inertia);
    } else if (this.inertia && this.y < 3) {
      return new PlayerFish(this.radius, this.clr, this.x, 990, this.lives, this.score, this.speed,
              this.velocityX, this.velocityY, this.inertia);
    } else if (this.inertia && Math.abs(this.velocityX) - this.accel() > 0) {
      return new PlayerFish(this.radius, this.clr,
              (int) (this.velocityX - (this.accel()) * this.multiply1(this.velocityX)) + this.x,
              this.y, this.lives, this.score, this.speed,
              this.velocityX + this.accel() * this.multiply1(this.velocityX) * -1, this.velocityY,
              this.inertia);
    } else if (this.inertia && Math.abs(this.velocityY) - this.accel() > 0) {
      return new PlayerFish(this.radius, this.clr, this.x,
              (int) (this.velocityY - this.accel() * this.multiply1(this.velocityY)) + this.y,
              this.lives, this.score, this.speed, this.velocityX,
              this.velocityY + this.accel() * this.multiply1(this.velocityY) * -1, this.inertia);
    } else if (this.inertia) {
      return new PlayerFish(this.radius, this.clr, this.x, this.y, this.lives, this.score,
              this.speed, 0, 0, false);
    } else {
      return this;
    }
  }

  // increases the size of the fish
  public PlayerFish getBigger(BkgdFish other) {
    return new PlayerFish(other.getBigger(this.radius), this.clr, this.x, this.y, this.lives,
            this.score + other.upScore(),
            this.speed, this.velocityX, this.velocityY, this.inertia);
  }

  // doubles player's max speed
  public PlayerFish upSpeed() {
    return new PlayerFish(this.radius, this.clr, this.x, this.y, this.lives, this.score,
            this.speed * 2, this.velocityX, this.velocityY, this.inertia);
  }

  // doubles the radius size
  public PlayerFish doubleSize() {
    return new PlayerFish(this.radius * 2, this.clr, this.x, this.y, this.lives, this.score,
            this.speed, this.velocityX, this.velocityY, this.inertia);
  }

  // generates world scene and adds live counters, max speed counter
  public WorldScene draw(WorldScene acc) {
    return acc.placeImageXY(new RectangleImage(2, 1000, "solid", Color.BLACK),
            1001, 500).placeImageXY(this.drawFish(), this.x,
            this.y).placeImageXY(new TextImage("Lives: " + this.lives,
            25, Color.RED), 1100, 350).placeImageXY(new TextImage("Score: "
            + this.score, 25, Color.ORANGE), 1100, 700).placeImageXY(new TextImage("Max Speed "
            + this.speed * 8, 25, Color.GREEN), 1100, 500);
  }
}


// represents fish swimming in the background
class BkgdFish extends AFish {
  boolean direction; // right is true, left is false
  String type;

  public BkgdFish(int radius, Color clr, int x, int y, boolean direction, String type) {
    super(radius, clr, x, y);
    this.direction = direction;
    this.type = type; // has to be one of "size", "speed", "none"
  }
  /* TEMPLATE:
     fields:
      this.radius ... int
      this.clr ... Color
      this.x ... int
      this.y ... int
      this.direction ... boolean
      this.type ... String
     methods:
      this.moveBkgd() ... BkgdFish
      this.getBigger(int) ... int
      this.upScore() ... int
      this.isConsumable() ... boolean
      this.consumableManager(PlayerFish) ... PlayerFish
   */

  // moves the background fish around
  BkgdFish moveBkgd() {
    if (this.x > 997 && this.direction) {
      return new BkgdFish(this.radius, this.clr, 10, this.y, this.direction, this.type);
    } else if (this.x < 3 && !this.direction) {
      return new BkgdFish(this.radius, this.clr, 990, this.y, this.direction, this.type);
    } else if (this.direction) {
      return new BkgdFish(this.radius, this.clr, this.x + 3, this.y, this.direction, this.type);
    } else {
      return new BkgdFish(this.radius, this.clr, this.x - 3, this.y, this.direction, this.type);
    }
  }

  // increases radius size of fish
  public int getBigger(int radius) {
    return (int)(this.radius * .2) + radius;
  }

  //increase the players current score based on the size fish they ate
  public int upScore() {
    return this.radius * 2;
  }

  // determines if the fish eaten is a speed booster or size increaser fish
  public boolean isConsumable() {
    return this.type.equals("speed") || this.type.equals("size");
  }

  // determines the effect of the consumed fish
  public PlayerFish consumableManager(PlayerFish other) {
    if (this.type.equals("speed")) {
      return other.upSpeed();
    } else {
      return other.doubleSize();
    }
  }

}

// represents a list of background fish
interface ILoFish {
  // checks if player fish collides with anything in the list
  FishWorld collideList(PlayerFish newFish, ILoFish acc);

  // moves all fish in the background
  ILoFish moveAllFish();

  // adds a fish to the background
  ILoFish addBkgdFish();

  // testable method to add background fish
  ILoFish addBkgdFishTest(int radius, int normalFishChance, int consumableFishChanceSpeed,
                          int consumableFishChanceSize);

  // draws the world scene
  WorldScene draw(WorldScene acc);

  // combines the list of fish
  ILoFish combine(ILoFish other);

  // checks if the player fish is bigger than all background fish
  boolean biggestFish(PlayerFish player);

  // chooses a color for the fish based on the radius size
  Color colorChooser(int radius);

  // chooses a random side for fish to spawn on
  int randomSide();

  // chooses a random side for fish to spawn on (for testing; no random element)
  int randomSideTest(int x);

  // chooses random direction for fish to move in
  boolean randomDirection();

  // testable version of randomDirection function
  boolean randomDirectionTest(int x);
}

// represents an empty list of background fish
class MtLoFish implements ILoFish {
  /* TEMPLATE:
      fields: none
      methods:
       this.collideList(PlayerFish, ILoFish) ... FishWorld
       this.moveAllFish() ... ILoFish
       this.addBkgdFish() ... ILoFish
       this.addBkgdFishTest(int, int, int, int) ... ILoFish
       this.draw(WorldScene) ... WorldScene
       this.combine(ILoFish) ... ILoFish
       this.biggestFish(PlayerFish) ... boolean
       this.colorChooser(int) ... Color
       this.randomSide() ... int
       this.randomSideTest(int) ... int
       this.randomDirection() ... boolean
       this.randomDirectionTest(int) ... boolean
   */
  // returns false because there are no fish to collide with
  public FishWorld collideList(PlayerFish newFish, ILoFish acc) {
    return new FishWorld(newFish, acc);
  }

  // moves all fish in the background
  public ILoFish moveAllFish() {
    return new MtLoFish();
  }

  // adds fish to the background
  public ILoFish addBkgdFish() {
    return this;
  }

  // testable method to add background fish
  public ILoFish addBkgdFishTest(int radius, int normalFishChance, int consumableFishChanceSpeed,
                                 int consumableFishChanceSize) {
    return this;
  }

  // draws the world
  public WorldScene draw(WorldScene acc) {
    return acc;
  }

  // combines the fish into a list of fish
  public ILoFish combine(ILoFish other) {
    return other;
  }

  // checks if the player fish is bigger than all background fish
  public boolean biggestFish(PlayerFish player) {
    return true;
  }

  // chooses a color for the fish based on the radius size
  public Color colorChooser(int radius) {
    if (radius < 50) {
      return Color.ORANGE;
    } else if (radius < 100) {
      return Color.RED;
    } else {
      return Color.BLACK;
    }
  }

  // picks a random side for the fish to spawn on
  public int randomSide() {
    int x = new Random().nextInt(2);
    if (x == 1) {
      return 990;
    } else {
      return x;
    }
  }

  // random side method for testing (no random)
  public int randomSideTest(int x) {
    if (x == 1) {
      return 990;
    } else {
      return x;
    }
  }

  // chooses a random direction for the fish to move in
  public boolean randomDirection() {
    int x = new Random().nextInt(2);
    return x == 1;
  }

  // testable version of randomDirection function
  public boolean randomDirectionTest(int x) {
    return x == 1;
  }
}

// represents a list of fish with at least one element
class ConsLoFish implements ILoFish {
  BkgdFish first;
  ILoFish rest;

  public ConsLoFish(BkgdFish first, ILoFish rest) {
    this.first = first;
    this.rest = rest;
  }
  /* TEMPLATE:
     fields:
      this.first ... BkgdFish
      this.rest ... ILoFish
     methods:
      this.collideList(PlayerFish, ILoFish) ... FishWorld
      this.moveAllFish() ... ILoFish
      this.colorChooser(int) ... Color
      this.randomSide() ... int
      this.randomSideTest(int) ... int
      this.randomDirection() ... boolean
      this.randomDirectionTest(int) ... boolean
      this.biggestFish(PlayerFish) ... boolean
      this.addBkgdFish() ... ILoFish
      this.addBkgdFishTest(int, int, int, int) ... ILoFish
      this.draw(WorldScene) ... WorldScene
      this.combine(ILoFish) ... ILoFish
   */

  // checks if the given fish collides with anything in the list
  public FishWorld collideList(PlayerFish newFish, ILoFish acc) {
    if (newFish.addRadius(this.first)
            > newFish.distanceFormula(this.first) && this.first.isConsumable()) {
      return new FishWorld(this.first.consumableManager(newFish), this.rest.combine(acc));
    }
    else if (newFish.addRadius(this.first) > newFish.distanceFormula(this.first)
            && first.eatFish(newFish)) {
      return new FishWorld(newFish.lostLife(), this.rest.combine(acc));
    } else if (newFish.addRadius(this.first) > newFish.distanceFormula(this.first)
            && newFish.eatFish(this.first)) {
      return new FishWorld(newFish.getBigger(this.first), this.rest.combine(acc));
    } else {
      return this.rest.collideList(newFish, new ConsLoFish(this.first, acc));
    }
  }


  // moves all the fish on the background
  public ILoFish moveAllFish() {
    return new ConsLoFish(this.first.moveBkgd(), this.rest.moveAllFish());
  }

  // chooses a color for the fish based on the radius size
  public Color colorChooser(int radius) {
    if (radius < 50) {
      return Color.ORANGE;
    } else if (radius < 100) {
      return Color.RED;
    } else {
      return Color.BLACK;
    }
  }

  // picks a random side for the fish to spawn on
  public int randomSide() {
    int x = new Random().nextInt(2);
    if (x == 1) {
      return 990;
    } else {
      return x;
    }
  }

  // random side method for testing (no random)
  public int randomSideTest(int x) {
    if (x == 1) {
      return 990;
    } else {
      return x;
    }
  }

  // chooses a random direction for the fish to move in
  public boolean randomDirection() {
    int x = new Random().nextInt(2);
    return x == 1;
  }

  // testable version of randomDirection function
  public boolean randomDirectionTest(int x) {
    return x == 1;
  }

  // checks if the player fish is bigger than all the background fish
  public boolean biggestFish(PlayerFish player) {
    return this.first.biggestFish(player) && this.rest.biggestFish(player);
  }

  // adds a fish to the background image
  public ILoFish addBkgdFish() {
    int radius = new Random().nextInt(100);
    int normalFishChance = new Random().nextInt(20);
    int consumableFishChanceSpeed = new Random().nextInt(500);
    int consumableFishChanceSize = new Random().nextInt(500);

    if (normalFishChance == 3) {
      return new ConsLoFish(new BkgdFish(radius, colorChooser(radius), randomSide(),
              new Random().nextInt(1000), randomDirection(), "none"),
              new ConsLoFish(this.first, this.rest));
    } else if (consumableFishChanceSpeed == 10) {
      return new ConsLoFish(new BkgdFish(20, Color.MAGENTA, randomSide(),
              new Random().nextInt(1000), randomDirection(), "speed"),
              new ConsLoFish(this.first, this.rest));
    } else if (consumableFishChanceSize == 49) {
      return new ConsLoFish(new BkgdFish(20, Color.BLUE, randomSide(),
              new Random().nextInt(1000), randomDirection(), "size"),
              new ConsLoFish(this.first, this.rest));
    } else {
      return this;
    }
  }

  // testable method to add background fish
  public ILoFish addBkgdFishTest(int radius, int normalFishChance, int consumableFishChanceSpeed,
                                 int consumableFishChanceSize) {
    if (normalFishChance == 3) {
      return new ConsLoFish(new BkgdFish(radius, colorChooser(radius), randomSideTest(2),
              500, randomDirectionTest(1), "none"),
              new ConsLoFish(this.first, this.rest));
    } else if (consumableFishChanceSpeed == 10) {
      return new ConsLoFish(new BkgdFish(20, Color.MAGENTA, randomSideTest(1),
              10, randomDirectionTest(2), "speed"),
              new ConsLoFish(this.first, this.rest));
    } else if (consumableFishChanceSize == 49) {
      return new ConsLoFish(new BkgdFish(20, Color.BLUE, randomSideTest(2),
              647, randomDirectionTest(1), "size"),
              new ConsLoFish(this.first, this.rest));
    } else {
      return this;
    }
  }

  // place a list of BkgdFish on the World Scene
  public WorldScene draw(WorldScene acc) {
    return this.rest.draw(this.first.draw(acc));
  }

  // combines the fish into the given list
  public ILoFish combine(ILoFish other) {
    return new ConsLoFish(this.first, this.rest.combine(other));
  }

}

// class that uses big bang
class FishWorld extends World {
  PlayerFish player;
  ILoFish bkgdFishes;

  FishWorld(PlayerFish player, ILoFish bkgdFishes) {
    this.player = player;
    this.bkgdFishes = bkgdFishes;
  }

  FishWorld(PlayerFish player) {

    BkgdFish bk1 = new BkgdFish(63, Color.RED, 50, 65, false, "none");
    BkgdFish bk2 = new BkgdFish(25, Color.orange, 34, 23, true, "none");
    BkgdFish bk3 = new BkgdFish(300, Color.BLACK, 0, 861, true, "none");

    ILoFish backgroundFish = new ConsLoFish(bk1, new ConsLoFish(bk2, new ConsLoFish(bk3,
            new MtLoFish())));
    this.player = player;
    this.bkgdFishes = backgroundFish;
  }

  /* TEMPLATE:
     fields:
      this.player ... PlayerFish
      this.bkgdFishes ... ILoFish
     methods:
      this.makeScene() ... WorldScene
      this.lastScene(String) ... WorldScene
      this.onKeyEvent(String) ... World
      this.onKeyReleased(String) ... World
      this.onTick() ... World
      this.slowDown() ... FishWorld
   */

  // edit draw class to add a black line and lives and score
  public WorldScene makeScene() {
    return this.bkgdFishes.draw(this.player.draw(new WorldScene(1200, 1000)));
  }

  // displays a game end scene with text
  public WorldScene lastScene(String s) {
    return new WorldScene(1200, 1000).placeImageXY(new TextImage(s, 50, Color.RED), 500, 600);
  }

  // changes the world state based on which arrow key is being pressed
  public World onKeyEvent(String s) {
    if (s.equals("up")) {
      return new FishWorld(this.player.setZero().moveY(this.player.accel() * -1), this.bkgdFishes);
    } else if (s.equals("right")) {
      return new FishWorld(this.player.setZero().moveX(this.player.accel()), this.bkgdFishes);
    } else if (s.equals("down")) {
      return new FishWorld(this.player.setZero().moveY(this.player.accel()), this.bkgdFishes);
    } else if (s.equals("left")) {
      return new FishWorld(this.player.setZero().moveX(this.player.accel() * -1), this.bkgdFishes);
    } else {
      return this;
    }
  }

  // when the arrow is pressed add 1% rounded of the circles to the velocity on the respective
  // axis and the add that amount + velocity to the x axis.
  //When the arrow is released send a true to the function in on Tick to start deceasing the
  // value by .2 each tick if another arrow is clicked it sets the velocity to
  public World onKeyReleased(String s) {
    if (s.equals("up")) {
      return new FishWorld(this.player.inertiaOn(), this.bkgdFishes);
    } else if (s.equals("right")) {
      return new FishWorld(this.player.inertiaOn(), this.bkgdFishes);
    } else if (s.equals("down")) {
      return new FishWorld(this.player.inertiaOn(), this.bkgdFishes);
    } else if (s.equals("left")) {
      return new FishWorld(this.player.inertiaOn(), this.bkgdFishes);
    } else {
      return this;
    }
  }

  // displays screen based on event
  public World onTick() {
    if (this.player.noLives()) {
      return this.endOfWorld("You ran out of lives");
    } else if (this.bkgdFishes.biggestFish(this.player)) {
      return this.endOfWorld("YOU WON!!!! ");
    } else {
      return this.bkgdFishes.moveAllFish().addBkgdFish().collideList(this.player,
              new MtLoFish()).slowDown();
    }
  }

  // testable version of on tick function
  public World onTickTest() {
    if (this.player.noLives()) {
      return this.endOfWorld("You ran out of lives");
    } else if (this.bkgdFishes.biggestFish(this.player)) {
      return this.endOfWorld("YOU WON!!!! ");
    } else {
      return this.bkgdFishes.moveAllFish().addBkgdFishTest(23, 10, 435, 210)
              .collideList(this.player, new MtLoFish()).slowDown();
    }
  }

  // calls slow down functions to return a fish world
  public FishWorld slowDown() {
    return new FishWorld(this.player.slowDown(), this.bkgdFishes);
  }

}

// examples of fish and tests
class ExamplesFish {
  PlayerFish pf1 = new PlayerFish(50, Color.GREEN, 500, 500, 3,
          0, 1, 0, 0, false);
  PlayerFish pf2 = new PlayerFish(60, Color.ORANGE, 1000, 550, 3,
          511, 2, 3, 0, true);
  PlayerFish pf3 = new PlayerFish(10, Color.RED, 997, 885, 3,
          91, 4, 0, 0, true);
  PlayerFish pf4 = new PlayerFish(15, Color.blue, 2, 100, 2,
          1300, 8, 0, 0, false);
  PlayerFish pf5 = new PlayerFish(22, Color.pink, 3, 15, 1,
          1600, 16, 0, 0, false);
  PlayerFish pf6 = new PlayerFish(35, Color.BLACK, 235, 999, 2,
          67, 32, 0, 0, true);
  PlayerFish pf7 = new PlayerFish(72, Color.blue, 203, 3, 1,
          105, 64, 0, 0, false);
  PlayerFish pf8 = new PlayerFish(35, Color.GREEN, 147, 1, 3,
          231, 128, 0, 0, true);
  PlayerFish pf9 = new PlayerFish(35, Color.GREEN, 147, 1, 0,
          12, 256, 0, 0, false);
  PlayerFish pf10 = new PlayerFish(15, Color.blue, 2, 100, 2,
          1300, 8, 0, 0, true);
  PlayerFish pf11 = new PlayerFish(150, Color.GREEN, 500, 500, 3,
          0, 1, 0, 0, false);
  PlayerFish pf12 = new PlayerFish(100, Color.RED, 997, 885, 3,
          91, 4, 0, 0, true);
  PlayerFish pf13 = new PlayerFish(200, Color.RED, 997, 885, 3,
          91, 4, 0, 0, true);
  PlayerFish pf14 = new PlayerFish(210, Color.RED, 997, 885, 3,
          91, 4, 0, 0, true);
  PlayerFish pf15 = new PlayerFish(10, Color.RED, 997, 885, 3,
          91, 4, 10, 20, true);
  PlayerFish pf16 = new PlayerFish(10, Color.RED, 997, 885, 3,
          91, 4, 0, 20, true);
  BkgdFish bk1 = new BkgdFish(63, Color.RED, 50, 65, false, "none");
  BkgdFish bk2 = new BkgdFish(25, Color.blue, 34, 23, true, "speed");
  BkgdFish bk3 = new BkgdFish(300, Color.BLACK, 0, 861, true, "none");
  BkgdFish bk4 = new BkgdFish(25, Color.pink, 423, 118, true, "none");
  BkgdFish bk5 = new BkgdFish(25, Color.pink, 998, 118, true, "size");
  BkgdFish bk6 = new BkgdFish(25, Color.pink, 998, 118, false, "none");
  BkgdFish bk7 = new BkgdFish(63, Color.RED, 2, 65, false, "none");
  BkgdFish bk8 = new BkgdFish(63, Color.RED, 2, 65, true, "none");
  BkgdFish bk9 = new BkgdFish(300, Color.BLACK, 997, 861, true, "none");
  BkgdFish bk10 = new BkgdFish(300, Color.BLACK, 997, 861, false, "none");
  BkgdFish bk11 = new BkgdFish(25, Color.pink, 3, 118, false, "none");
  BkgdFish bk12 = new BkgdFish(25, Color.pink, 3, 118, true, "none");

  ILoFish backgroundFish = new ConsLoFish(this.bk1, new ConsLoFish(this.bk2,
          new ConsLoFish(this.bk3, new MtLoFish())));

  ILoFish mt = new MtLoFish();
  ILoFish lst1 = new ConsLoFish(this.bk1, this.mt);
  ILoFish lst2 = new ConsLoFish(this.bk2, this.mt);
  ILoFish lst3 = new ConsLoFish(this.bk6, this.mt);
  ILoFish lst4 = new ConsLoFish(this.bk7, this.lst3);
  ILoFish lst5 = new ConsLoFish(this.bk8, this.lst4);

  FishWorld fw1 = new FishWorld(this.pf1, this.lst2);
  FishWorld fw2 = new FishWorld(this.pf2, this.mt);
  FishWorld fw3 = new FishWorld(this.pf3, this.lst4);
  FishWorld fw4 = new FishWorld(this.pf4, this.lst5);
  FishWorld fw5 = new FishWorld(this.pf9, this.lst5);

  // testing the method that checks if one fish can eat another fish
  boolean testEatFish(Tester t) {
    return t.checkExpect(this.pf1.eatFish(this.bk1), false)
            && t.checkExpect(this.bk1.eatFish(this.pf1), true)
            && t.checkExpect(this.bk1.eatFish(this.bk1), true);
  }

  // testing the method that calculates the distance between two fish
  boolean testDistanceFormula(Tester t) {
    return t.checkInexact(this.pf1.distanceFormula(this.bk1), 625.87938, 0.001)
            && t.checkInexact(this.pf1.distanceFormula(this.bk2), 666.84705, 0.001)
            && t.checkInexact(this.bk1.distanceFormula(this.bk2), 44.9444, 0.001);
  }

  // testing the method that adds the radii of two fish
  boolean testAddRadius(Tester t) {
    return t.checkExpect(this.pf1.addRadius(this.bk1), 113)
            && t.checkExpect(this.pf1.addRadius(this.bk2), 75)
            && t.checkExpect(this.bk1.addRadius(this.bk2), 88);
  }

  // testing the method to draw the fish
  boolean testDrawFish(Tester t) {
    return t.checkExpect(this.pf1.drawFish(), new CircleImage(50, "solid", Color.GREEN))
            && t.checkExpect(this.bk1.drawFish(), new CircleImage(63, "solid", Color.RED))
            && t.checkExpect(this.bk4.drawFish(), new CircleImage(25, "solid", Color.pink));
  }

  // testing the method to see if one fish is bigger than another
  boolean testBiggestFish(Tester t) {
    return t.checkExpect(this.pf1.biggestFish(this.bk1), true)
            && t.checkExpect(this.bk1.biggestFish(this.pf1), false)
            && t.checkExpect(this.bk2.biggestFish(this.bk3), true)
            && t.checkExpect(this.bk2.biggestFish(this.bk4), false)
            && t.checkExpect(this.mt.biggestFish(this.pf3), true)
            && t.checkExpect(this.lst2.biggestFish(this.pf7), true)
            && t.checkExpect(this.lst2.biggestFish(this.pf1), true);
  }

  // testing the method to move player fish in X direction & ensure it stays within the X range
  boolean testMoveX(Tester t) {
    return t.checkExpect(this.pf2.moveX(3), new PlayerFish(60, Color.ORANGE, 10, 550, 3, 511,
            2, 3, 0, true))
            && t.checkExpect(this.pf3.moveX(3), new PlayerFish(10, Color.RED, 1000, 885, 3, 91,
            4, 3, 0, true))
            && t.checkExpect(this.pf4.moveX(-3), new PlayerFish(15, Color.blue, 990, 100, 2, 1300,
            8, 0, 0, false))
            && t.checkExpect(this.pf5.moveX(-3), new PlayerFish(22, Color.pink, 0, 15, 1, 1600,
            16, -3, 0, false))
            && t.checkExpect(this.pf1.moveX(5), new PlayerFish(50, Color.GREEN, 505, 500, 3, 0,
            1, 5, 0, false))
            && t.checkExpect(this.pf7.moveX(-10), new PlayerFish(72, Color.blue, 193, 3, 1, 105,
            64, -10, 0, false));
  }

  // testing the method to move player fish in Y direction & ensure it stays within range
  boolean testMoveY(Tester t) {
    return t.checkExpect(this.pf1.moveY(10), new PlayerFish(50, Color.GREEN, 500, 508, 3, 0, 1,
            0, 8, false))
            && t.checkExpect(this.pf3.moveY(-3), new PlayerFish(10, Color.RED, 997, 882, 3, 91,
            4, 0, -3, true))
            && t.checkExpect(this.pf6.moveY(4), new PlayerFish(35, Color.BLACK, 235, 10, 2, 67,
            32, 0, 0, true))
            && t.checkExpect(this.pf3.moveY(3), new PlayerFish(10, Color.RED, 997, 888, 3, 91,
            4, 0, 3, true))
            && t.checkExpect(this.pf7.moveY(-2), new PlayerFish(72, Color.blue, 203, 1, 1, 105,
            64, 0, -2, false))
            && t.checkExpect(this.pf8.moveY(-5), new PlayerFish(35, Color.GREEN, 147, 990, 3, 231,
            128, 0, 0, true));
  }

  // testing the method that reduces life count by 1
  boolean testLostLife(Tester t) {
    return t.checkExpect(this.pf1.lostLife(), new PlayerFish(50, Color.GREEN, 500, 500, 2,
            0, 1, 0, 0, false))
            && t.checkExpect(this.pf4.lostLife(), new PlayerFish(15, Color.blue, 2, 100, 1,
            1300, 8, 0, 0, false))
            && t.checkExpect(this.pf7.lostLife(), new PlayerFish(72, Color.blue, 203, 3, 0,
            105, 64, 0, 0, false));
  }

  // testing the method to see if the player fish has 0 lives
  boolean testNoLives(Tester t) {
    return t.checkExpect(this.pf1.noLives(), false)
            && t.checkExpect(this.pf7.noLives(), false)
            && t.checkExpect(this.pf9.noLives(), true);
  }

  // testing the method to increase fish size
  boolean testGetBigger(Tester t) {
    return t.checkExpect(this.bk1.getBigger(10), 22)
            && t.checkExpect(this.pf7.getBigger(this.bk1),
            new PlayerFish(84, Color.blue, 203, 3, 1, 231, 64, 0, 0, false))
            && t.checkExpect(this.pf1.getBigger(this.bk3),
            new PlayerFish(110, Color.GREEN, 500, 500, 3, 600, 1, 0, 0, false))
            && t.checkExpect(this.bk4.getBigger(-10), -5);
  }

  // testing the method to move background fish and keep them within range
  boolean testMoveBkgd(Tester t) {
    return t.checkExpect(this.bk1.moveBkgd(),
            new BkgdFish(63, Color.RED, 47, 65, false, "none"))
            && t.checkExpect(this.bk2.moveBkgd(),
            new BkgdFish(25, Color.blue, 37, 23, true, "speed"))
            && t.checkExpect(this.bk5.moveBkgd(),
            new BkgdFish(25, Color.pink, 10, 118, true, "size"))
            && t.checkExpect(this.bk6.moveBkgd(),
            new BkgdFish(25, Color.pink, 995, 118, false, "none"))
            && t.checkExpect(this.bk7.moveBkgd(),
            new BkgdFish(63, Color.RED, 990, 65, false, "none"))
            && t.checkExpect(this.bk8.moveBkgd(),
            new BkgdFish(63, Color.RED, 5, 65, true, "none"))
            && t.checkExpect(this.bk9.moveBkgd(),
            new BkgdFish(300, Color.BLACK, 1000, 861, true, "none"))
            && t.checkExpect(this.bk10.moveBkgd(),
            new BkgdFish(300, Color.BLACK, 994, 861, false, "none"))
            && t.checkExpect(this.bk11.moveBkgd(),
            new BkgdFish(25, Color.pink, 0, 118, false, "none"))
            && t.checkExpect(this.bk12.moveBkgd(),
            new BkgdFish(25, Color.pink, 6, 118, true, "none"));
  }

  // testing the method to move all fish in the background
  boolean testMoveAllFish(Tester t) {
    return t.checkExpect(this.mt.moveAllFish(), this.mt)
            && t.checkExpect(this.lst2.moveAllFish(),
            new ConsLoFish(new BkgdFish(25, Color.blue, 37, 23, true,
                    "speed"), this.mt));
  }

  // testing the method to combine fish into a list of fish
  boolean testCombine(Tester t) {
    return t.checkExpect(this.mt.combine(this.lst1), this.lst1)
            && t.checkExpect(this.lst1.combine(this.lst2),
            new ConsLoFish(new BkgdFish(63, Color.RED, 50, 65, false,
                    "none"), new ConsLoFish(new BkgdFish(25, Color.blue,
                    34, 23, true, "speed"), this.mt)));
  }

  // test the method to choose a color based on radius
  boolean testColorChooser(Tester t) {
    return t.checkExpect(this.lst1.colorChooser(100), Color.BLACK)
            && t.checkExpect(this.lst1.colorChooser(99), Color.RED)
            && t.checkExpect(this.lst1.colorChooser(200), Color.BLACK);
  }

  // test the method to create game world
  boolean testMakeScene(Tester t) {
    return t.checkExpect(this.fw1.makeScene(),
            this.lst2.draw(this.pf1.draw(new WorldScene(1200,1000))))
            && t.checkExpect(this.fw2.makeScene(),
            this.mt.draw(this.pf2.draw(new WorldScene(1200,1000))))
            && t.checkExpect(this.fw4.makeScene(),
            this.lst5.draw(this.pf4.draw(new WorldScene(1200,1000))));
  }

  // test the method to diplay a game end scene with text
  boolean testLastScene(Tester t) {
    return t.checkExpect(this.fw1.lastScene("hello"),
            new WorldScene(1200,1000).placeImageXY(new TextImage("hello",
                    50, Color.RED), 500, 600))
            && t.checkExpect(this.fw2.lastScene(""),
            new WorldScene(1200,1000).placeImageXY(new TextImage("",
                    50, Color.RED), 500, 600))
            && t.checkExpect(this.fw3.lastScene("YOU WON!!!! "),
            new WorldScene(1200,1000).placeImageXY(new TextImage("YOU WON!!!! ",
                    50, Color.RED), 500, 600));
  }

  // test method to change world state based on key pressed
  boolean testOnKeyEvent(Tester t) {
    return t.checkExpect(this.fw1.onKeyEvent("up"), new FishWorld(this.pf1.setZero()
            .moveY(this.pf1.accel() * -1), this.lst2))
            && t.checkExpect(this.fw2.onKeyEvent("right"), new FishWorld(this.pf2.setZero()
            .moveX(this.pf2.accel()), this.mt))
            && t.checkExpect(this.fw3.onKeyEvent("down"), new FishWorld(this.pf3.setZero()
            .moveY(this.pf3.accel()), this.lst4))
            && t.checkExpect(this.fw4.onKeyEvent("left"), new FishWorld(this.pf4.setZero()
            .moveX(this.pf4.accel() * -1), this.lst5))
            && t.checkExpect(this.fw1.onKeyEvent("k"), this.fw1);
  }

  // testing the method to handle released keys
  boolean testOnKeyReleased(Tester t) {
    return t.checkExpect(this.fw1.onKeyReleased("up"), new FishWorld(this.pf1.inertiaOn(),
            this.lst2))
            && t.checkExpect(this.fw2.onKeyReleased("right"), new FishWorld(this.pf2.inertiaOn(),
            this.mt))
            && t.checkExpect(this.fw3.onKeyReleased("down"), new FishWorld(this.pf3.inertiaOn(),
            this.lst4))
            && t.checkExpect(this.fw4.onKeyReleased("left"), new FishWorld(this.pf4.inertiaOn(),
            this.lst5))
            && t.checkExpect(this.fw1.onKeyReleased("k"), this.fw1);
  }

  // testing the method to display screen based on event
  boolean testOnTick(Tester t) {
    return t.checkExpect(this.fw5.onTick(), this.fw5.endOfWorld("You ran out of lives"))
            && t.checkExpect(this.fw1.onTick(), this.fw1.endOfWorld("YOU WON!!!! "));
  }

  // testing the testable version of the method to display screen based on event
  boolean testOnTickTest(Tester t) {
    return t.checkExpect(this.fw5.onTickTest(), this.fw5.endOfWorld("You ran out of lives"))
            && t.checkExpect(this.fw1.onTickTest(), this.fw1.endOfWorld("YOU WON!!!! "))
            && t.checkExpect(this.fw4.onTickTest(), this.lst5.moveAllFish()
            .addBkgdFishTest(23, 10, 435, 210)
            .collideList(this.pf4, new MtLoFish()).slowDown());
  }

  // testing the method to slow down fish (for PlayerFish and FishWorld)
  boolean testSlowDown(Tester t) {
    return t.checkExpect(this.pf2.slowDown(), new PlayerFish(60, Color.ORANGE, 10, 550, 3, 511,
            2, 3, 0, true))
            && t.checkExpect(this.pf10.slowDown(), new PlayerFish(15, Color.blue, 990, 100, 2,
            1300, 8, 0, 0, true))
            && t.checkExpect(this.pf6.slowDown(), new PlayerFish(35, Color.BLACK, 235, 10, 2, 67,
            32, 0, 0, true))
            && t.checkExpect(this.pf8.slowDown(), new PlayerFish(35, Color.GREEN, 147, 990, 3, 231,
            128, 0, 0, true))
            & t.checkExpect(this.pf15.slowDown(), new PlayerFish(10, Color.RED,
            (int) (10 - (this.pf15.accel()) + 997),
            885, 3, 91, 4, 10 + this.pf15.accel()
            * -1, 20, true))
            && t.checkExpect(this.pf16.slowDown(), new PlayerFish(10, Color.RED, 997,
            (int) (20 - this.pf16.accel() + 885), 3, 91, 4, 0,
            20 + this.pf16.accel() * -1, true))
            && t.checkExpect(this.pf9.slowDown(), this.pf9)
            && t.checkExpect(this.fw1.slowDown(), new FishWorld(this.pf1.slowDown(), this.lst2))
            && t.checkExpect(this.fw2.slowDown(), new FishWorld(this.pf2.slowDown(), this.mt));
  }

  // testing the method that chooses a random side for fish to spawn (no random)
  boolean testRandomSideTest(Tester t) {
    return t.checkExpect(this.lst1.randomSideTest(1), 990)
            && t.checkExpect(this.lst1.randomSideTest(2), 2)
            && t.checkExpect(this.mt.randomSideTest(1), 990)
            && t.checkExpect(this.mt.randomSideTest(2), 2);
  }

  boolean testRandomDirectionTest(Tester t) {
    return t.checkExpect(this.lst2.randomDirectionTest(1), true)
            && t.checkExpect(this.mt.randomDirectionTest(1), true)
            && t.checkExpect(this.lst2.randomDirectionTest(2), false)
            && t.checkExpect(this.mt.randomDirectionTest(2), false);
  }

  // testing helper function that makes sure correct signs are being used
  boolean testMultiply1(Tester t) {
    return t.checkExpect(this.pf1.multiply1(0), 1)
            && t.checkExpect(this.pf2.multiply1(1), 1)
            && t.checkExpect(this.pf3.multiply1(-1), -1);
  }

  // testing the method to create acceleration
  boolean testAccel(Tester t) {
    return t.checkInexact(this.pf1.accel(), 0.2, 0.0001)
            && t.checkInexact(this.pf11.accel(), 0.13, 0.0001)
            && t.checkInexact(this.pf12.accel(), 0.2, 0.0001)
            && t.checkInexact(this.pf13.accel(), 0.15, 0.0001)
            && t.checkInexact(this.pf14.accel(), 0.14, 0.0001);
  }

  // testing the method to turn inertia on
  boolean testInertiaOn(Tester t) {
    return t.checkExpect(this.pf2.inertiaOn(), this.pf2)
            && t.checkExpect(this.pf1.inertiaOn(), new PlayerFish(50, Color.GREEN,
            500, 500, 3, 0, 1, 0, 0, true));
  }

  // testing the method to determine if a fish is consumable
  boolean testIsConsumable(Tester t) {
    return t.checkExpect(this.bk1.isConsumable(), false)
            && t.checkExpect(this.bk2.isConsumable(), true)
            && t.checkExpect(this.bk5.isConsumable(), true);
  }

  // testing the method that increases player fish size based on fish eaten
  boolean testUpScore(Tester t) {
    return t.checkExpect(this.bk1.upScore(), 126)
            && t.checkExpect(this.bk2.upScore(), 50)
            && t.checkExpect(this.bk3.upScore(), 600);
  }

  // testing the method that doubles radius size
  boolean testDoubleSize(Tester t) {
    return t.checkExpect(this.pf1.doubleSize(), new PlayerFish(100, Color.GREEN,
            500, 500, 3, 0, 1, 0, 0, false))
            && t.checkExpect(this.pf8.doubleSize(), new PlayerFish(70, Color.GREEN,
            147, 1, 3, 231, 128, 0, 0, true));
  }

  // testing the method that increases max speed
  boolean testUpSpeed(Tester t) {
    return t.checkExpect(this.pf2.upSpeed(), new PlayerFish(60, Color.ORANGE,
            1000, 550, 3, 511, 4, 3, 0, true))
            && t.checkExpect(this.pf5.upSpeed(), new PlayerFish(22, Color.pink,
            3, 15, 1, 1600, 32, 0, 0, false));
  }

  // testing the method that determines the effect of the consumed fish
  boolean testConsumableManager(Tester t) {
    return t.checkExpect(this.bk2.consumableManager(this.pf1), this.pf1.upSpeed())
            && t.checkExpect(this.bk1.consumableManager(this.pf2), this.pf2.doubleSize())
            && t.checkExpect(this.bk5.consumableManager(this.pf6), this.pf6.doubleSize());
  }

  // testing the method to add fish to background (testable version)
  boolean testAddBkgdFish(Tester t) {
    return t.checkExpect(this.mt.addBkgdFishTest(23, 10, 435, 210), this.mt)
            && t.checkExpect(this.lst2.addBkgdFishTest(23, 3, 435, 210),
            new ConsLoFish(new BkgdFish(23, Color.ORANGE, 2, 500, true, "none"),
                    new ConsLoFish(this.bk2, this.mt)))
            && t.checkExpect(this.lst5.addBkgdFishTest(23, 10, 10, 210),
            new ConsLoFish(new BkgdFish(20, Color.MAGENTA, 990, 10,
                    false, "speed"), new ConsLoFish(this.bk8, this.lst4)))
            && t.checkExpect(this.lst5.addBkgdFishTest(23, 10, 435, 49),
            new ConsLoFish(new BkgdFish(20, Color.BLUE, 2, 647,
                    true, "size"), new ConsLoFish(this.bk8, this.lst4)))
            && t.checkExpect(this.lst5.addBkgdFishTest(23, 10, 435, 210), this.lst5);
  }

  // testing the method to run the game
  boolean testRunGame(Tester t) {
    return new FishWorld(this.pf1, this.backgroundFish).bigBang(1200, 1000, .05);
  }
}