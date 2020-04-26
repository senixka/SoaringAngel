package com.mygdx.game.NPCs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Dialogue;
import com.mygdx.game.Helper;
import com.mygdx.game.MyGame;
import com.mygdx.game.NPC;
import com.mygdx.game.PauseMenuScreen;
import com.mygdx.game.Potions.EnergyPotion;
import com.mygdx.game.Potions.HealthPotion;
import com.mygdx.game.Shop;
import com.mygdx.game.ShopScreen;
import com.mygdx.game.Weapon;
import com.mygdx.game.Weapons.FirstGun;
import com.mygdx.game.World;

public class Seller extends NPC {
    public static final Texture IMG = new Texture(Gdx.files.internal("Seller.png"));
    public static final Texture HeadIMG = new Texture(Gdx.files.internal("SellerHead.png"));
    public static final int sizeX = 40, sizeY = 80;
    public Shop shop;

    public Seller(float x, float y) {
        super(IMG, x, y, sizeX, sizeY);
        shop = new Shop();
        shop.add(new HealthPotion());
        shop.add(new EnergyPotion());
        for (int i = 0; i < 5; i++) {
            shop.add(Helper.getRandomWeapon());
        }
    }


    @Override
    public boolean talk() {
        if (Helper.dist(World.pers.getCenter(), getCenter()) < 100) {
            World.controller.setDialogue(createDialogue());
            return true;
        }
        return false;
    }

    @Override
    public void finalTalk() {
        MyGame.staticSetScreen(new ShopScreen(shop));
    }

    public Dialogue createDialogue() {
        Dialogue d = new Dialogue(this);
        d.addSpeechFrame(HeadIMG, "Hey, Dude! Do you want to by something interesting. I'm sure, you'll like it");
        d.addSpeechFrame(HeadIMG, "Don't hesitate. Come on!");
        d.addSpeechFrame(null, "*thoughts* I think I really can find something useful here");
        return d;
    }
}
