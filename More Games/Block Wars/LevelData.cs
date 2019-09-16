using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using FarseerPhysics.Dynamics;
using FarseerPhysics.Factories;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework;

namespace Block_Wars
{
    public static class LevelData
    {
        public static int highestLevel = 4;
        public static int screenWidth = 1000;
        public static int screenHeight = 600;

        public static void LoadLevel(Game1 game, World world, Texture2D texture, ref List<Block> blocks, ref List<int> balls, int level)
        {
            Block block;
            switch (level)
            {
                case 1:
                    block = new Block(world, texture, new Vector2(500,50), new Vector2(screenWidth - 275, screenHeight - 100), 1, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(50,100), new Vector2(screenWidth - 275, screenHeight - 220), 2, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(50,200), new Vector2(screenWidth - 275, screenHeight - 340), 1, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(100,100), new Vector2(screenWidth - 275, screenHeight - 550), 2, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    balls.Add(-1);
                    balls.Add(-1);
                    break;
                case 2:
                    block = new Block(world, texture, new Vector2(50,200), new Vector2(screenWidth - 225, screenHeight - 100), 2, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(50,200), new Vector2(screenWidth - 375, screenHeight - 100), 2, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(200,50), new Vector2(screenWidth - 300, screenHeight - 300), 3, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(50,200), new Vector2(screenWidth - 225, screenHeight - 400), 2, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(50,200), new Vector2(screenWidth - 375, screenHeight - 400), 2, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(200,50), new Vector2(screenWidth - 300, screenHeight - 550), 3, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    balls.Add(-1);
                    balls.Add(-1);
                    balls.Add(-1);
                    break;
                case 3:
                    block = new Block(world, texture, new Vector2(50,50), new Vector2(screenWidth - 225, screenHeight - 50), 1, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(50,50), new Vector2(screenWidth - 375, screenHeight - 50), 1, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(200,100), new Vector2(screenWidth - 300, screenHeight - 150), 3, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(50,50), new Vector2(screenWidth - 225, screenHeight - 250), 1, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(50,50), new Vector2(screenWidth - 375, screenHeight - 250), 1, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(50,50), new Vector2(screenWidth - 225, screenHeight - 300), 3, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(50,50), new Vector2(screenWidth - 375, screenHeight - 300), 3, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(200,100), new Vector2(screenWidth - 300, screenHeight - 400), 1, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(50,50), new Vector2(screenWidth - 225, screenHeight - 450), 3, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(50,50), new Vector2(screenWidth - 375, screenHeight - 450), 3, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    balls.Add(3);
                    balls.Add(1);
                    break;
                case 4:
                    block = new Block(world, texture, new Vector2(25,100), new Vector2(screenWidth - 225, screenHeight - 100), 3, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(25,100), new Vector2(screenWidth - 300, screenHeight - 100), 2, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(25,100), new Vector2(screenWidth - 375, screenHeight - 100), 3, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(25,100), new Vector2(screenWidth - 450, screenHeight - 100), 2, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(250,25), new Vector2(screenWidth - 337, screenHeight - 175), 1, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(25,100), new Vector2(screenWidth - 225, screenHeight - 250), 3, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(25,100), new Vector2(screenWidth - 300, screenHeight - 250), 2, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(25,100), new Vector2(screenWidth - 375, screenHeight - 250), 3, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(175,25), new Vector2(screenWidth - 300, screenHeight - 300), 1, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(25,100), new Vector2(screenWidth - 225, screenHeight - 375), 3, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(25,100), new Vector2(screenWidth - 300, screenHeight - 375), 2, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(100,25), new Vector2(screenWidth - 262, screenHeight - 450), 1, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(25,100), new Vector2(screenWidth - 225, screenHeight - 525), 3, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    block = new Block(world, texture, new Vector2(100,550), new Vector2(screenWidth - 100, screenHeight - 350), 1, true);
                    blocks.Add(block);
                    block.body.OnCollision += game.OnCollision;

                    balls.Add(1);
                    balls.Add(1);
                    break;
                default:
                    break;
            }
        }

        public static int GetPowerInt(int power)
        {
            int powInt;

            if (power < 50)
                powInt = 1;
            else if (power < 100)
                powInt = 2;
            else if (power < 150)
                powInt = 3;
            else if (power < 200)
                powInt = 4;
            else if (power < 250)
                powInt = 5;
            else if (power < 300)
                powInt = 6;
            else if (power < 350)
                powInt = 7;
            else
                powInt = 8;

            return powInt;
        }

        public static Color GetColor(int n)
        {
            switch (n)
            {
                case 0: return Color.Black;
                case 1: return Color.Green;
                case 2: return Color.Orange;
                case 3: return Color.Purple;
                default: return Color.White;
            }
        }
    }
}
