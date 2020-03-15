package com.mygdx.game.NPCs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Helper;
import com.mygdx.game.MyGame;
import com.mygdx.game.NPC;
import com.mygdx.game.PauseMenuScreen;
import com.mygdx.game.Shop;
import com.mygdx.game.ShopScreen;
import com.mygdx.game.Weapons.FirstGun;
import com.mygdx.game.World;

public class Seller extends NPC {
    public static final Texture IMG = new Texture(Gdx.files.internal("Seller.png"));
    public static final int sizeX = 40, sizeY = 80;
    public Shop shop;

    public Seller(float x, float y) {
        super(IMG, x, y, sizeX, sizeY);
        shop = new Shop();
        shop.add(new FirstGun());
    }

    @Override
    public void talk() {
        if (Helper.dist(World.pers.getCenter(), getCenter()) < 100) {
            MyGame.staticSetScreen(new ShopScreen(shop));
        }
    }
}
