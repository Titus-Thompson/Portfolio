using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.GamerServices;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Media;
using FarseerPhysics.Dynamics;
using FarseerPhysics.Factories;

namespace Block_Wars
{
    public class Game1 : Microsoft.Xna.Framework.Game
    {
        GraphicsDeviceManager graphics;
        SpriteBatch spriteBatch;
        SpriteFont msgFont, regFont;

        World world;
        Texture2D blockTexture, ballTexture, cannonTexture, powerBar, logo;

        SoundEffect soundCannon, music;
        SoundEffectInstance musicInstance;

        Block floor;
        List<Block> blockList;
        List<int> ballList;

        Vector2 ballPosition = new Vector2(300, LevelData.screenHeight - 100);

        Vector2 cannonPosition;
        int cannonPower, powerInt;
        float cannonAngle;
        bool powerUp;

        int currentLevel;

        KeyboardState keyboardState, oldKeyboardState;

        public Vector2 box, oldloc, newloc;
        private bool leftRight;
        private bool moving, set;
        private int multiplier;
        TimeSpan startTime;
        Texture2D boxTexture;

        enum GameState
        {
            TitleScreen,
            LevelSelect,
            LoadLevel,
            Level,
            LevelWon,
            LevelLose
        }
        GameState gameState;

        const float unitToPixel = 100.0f;
        const float pixelToUnit = 1 / unitToPixel;

        public Game1()
        {
            graphics = new GraphicsDeviceManager(this);
            graphics.PreferredBackBufferWidth = LevelData.screenWidth;
            graphics.PreferredBackBufferHeight = LevelData.screenHeight;
            Window.AllowUserResizing = false;
            Content.RootDirectory = "Content";

            gameState = GameState.TitleScreen;
        }

        protected override void Initialize()
        {
            base.Initialize();
        }

        protected override void LoadContent()
        {
            spriteBatch = new SpriteBatch(GraphicsDevice);

            blockTexture = Content.Load<Texture2D>("Block");
            ballTexture = Content.Load<Texture2D>("Ball");
            cannonTexture = Content.Load<Texture2D>("Cannon");
            powerBar = Content.Load<Texture2D>("PowerBar");
            logo = Content.Load<Texture2D>("BlockLogo");
            boxTexture = Content.Load<Texture2D>("Marbles");

            music = Content.Load<SoundEffect>("16 bit Snowflakes");
            soundCannon = Content.Load<SoundEffect>("Bang");
            musicInstance = music.CreateInstance();

            msgFont = Content.Load<SpriteFont>("MsgFont");
            regFont = Content.Load<SpriteFont>("RegFont");

            keyboardState = Keyboard.GetState();

            currentLevel = 1;
            box = new Vector2(0, 0);
            oldloc = new Vector2(0);
            newloc = new Vector2(0);
            moving = false;
            multiplier = 1;
            leftRight = true;
            set = false;
        }

        protected override void UnloadContent()
        {
            
        }

        protected override void Update(GameTime gameTime)
        {
            oldKeyboardState = keyboardState;
            keyboardState = Keyboard.GetState();

            if (musicInstance.State == SoundState.Stopped)
            {
                musicInstance.Volume = 0.75f;
                musicInstance.IsLooped = true;
                musicInstance.Play();
            }
            else
                musicInstance.Resume();

            switch (gameState)
            {
                case GameState.TitleScreen: UpdateTitleScreen(); break;
                case GameState.LevelSelect: UpdateLevelSelect(gameTime); break;
                case GameState.LoadLevel: LoadLevel(); break;
                case GameState.Level: UpdateLevel(gameTime); break;
                case GameState.LevelLose: UpdateLose(); break;
                case GameState.LevelWon: UpdateWon();  break;
                default: break;
            }
            if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed)
                this.Exit();

            base.Update(gameTime);
        }

        private void UpdateTitleScreen()
        {
            if (keyboardState.IsKeyDown(Keys.Space) && oldKeyboardState.IsKeyUp(Keys.Space) || keyboardState.IsKeyDown(Keys.Enter) && oldKeyboardState.IsKeyUp(Keys.Enter))
            {
                gameState = GameState.LevelSelect;
            }
        }

        private void UpdateLevelSelect(GameTime gameTime)
        {
            if (keyboardState.IsKeyDown(Keys.Up))
                Move(0);
            else if (keyboardState.IsKeyDown(Keys.Right))
                Move(1);
            else if (keyboardState.IsKeyDown(Keys.Down))
                Move(2);
            else if (keyboardState.IsKeyDown(Keys.Left))
                Move(3);

            MoveBox(gameTime);

            if (keyboardState.IsKeyDown(Keys.Space) && oldKeyboardState.IsKeyUp(Keys.Space) || keyboardState.IsKeyDown(Keys.Enter) && oldKeyboardState.IsKeyUp(Keys.Enter))
            {
                gameState = GameState.Level;
                LoadLevel();
            }

            if (keyboardState.IsKeyDown(Keys.Escape) && oldKeyboardState.IsKeyUp(Keys.Escape))
            {
                gameState = GameState.TitleScreen;
            }
        }

        private void LoadLevel()
        {
            world = new World(new Vector2(0, 9.8f));
            floor = new Block(world, blockTexture, new Vector2(1200, 100), new Vector2(GraphicsDevice.Viewport.Width, 100.0f), 0, true);
            floor.Position = new Vector2(GraphicsDevice.Viewport.Width / 2.0f, GraphicsDevice.Viewport.Height);
            floor.body.BodyType = BodyType.Static;
            blockList = new List<Block>();
            ballList = new List<int>();

            LevelData.LoadLevel(this, world, blockTexture, ref blockList, ref ballList, currentLevel);
            cannonPosition = new Vector2(200, LevelData.screenHeight - 70);
            cannonPower = 25;
            powerInt = LevelData.GetPowerInt(cannonPower);
            cannonAngle = (float)Math.PI / 4;
            powerUp = true;

            gameState = GameState.Level;
        }

        private void UpdateLevel(GameTime gameTime)
        {
            world.Step((float)gameTime.ElapsedGameTime.TotalSeconds);

            //Check if level is won.
            bool won = true;
            foreach (Block b in blockList)
                if (b.colorNum > 0)
                    won = false;
            if (won)
            {
                gameState = GameState.LevelWon;
                return;
            }

            if (powerUp)
            {
                cannonPower += 5;
                if (cannonPower == 375)
                    powerUp = false;
            }
            else
            {
                cannonPower -= 5;
                if (cannonPower == 25)
                    powerUp = true;
            }
            powerInt = LevelData.GetPowerInt(cannonPower);

            if (keyboardState.IsKeyDown(Keys.Space) && oldKeyboardState.IsKeyUp(Keys.Space) || keyboardState.IsKeyDown(Keys.Enter) && oldKeyboardState.IsKeyUp(Keys.Enter))
            {
                //Check if level is lost.
                bool lost = false;
                if (ballList.Count == 0)
                    lost = true;
                if (lost)
                {
                    gameState = GameState.LevelLose;
                    return;
                }

                soundCannon.Play();
                Block ball = new Block(world, ballTexture, new Vector2(50,0), new Vector2(cannonPosition.X, cannonPosition.Y), ballList.ElementAt(0), false);
                ball.body.ApplyForce(new Vector2(cannonPower * (float)Math.Cos(cannonAngle), -1 * cannonPower * (float)Math.Sin(cannonAngle)));
                blockList.Add(ball);
                ballList.RemoveAt(0);
            }

            if (keyboardState.IsKeyDown(Keys.Up) && cannonAngle < Math.PI / 2)
            {
                cannonAngle += (float)Math.PI / 300;
            }
            if (keyboardState.IsKeyDown(Keys.Down) && cannonAngle > 0)
            {
                cannonAngle -= (float)Math.PI / 300;
            }

            if (keyboardState.IsKeyDown(Keys.R) && oldKeyboardState.IsKeyUp(Keys.R))
            {
                LoadLevel();
            }

            if (keyboardState.IsKeyDown(Keys.Escape) && oldKeyboardState.IsKeyUp(Keys.Escape))
            {
                gameState = GameState.LevelSelect;
            }
        }

        private void UpdateLose()
        {
            if (keyboardState.IsKeyDown(Keys.Space) && oldKeyboardState.IsKeyUp(Keys.Space) || keyboardState.IsKeyDown(Keys.Enter) && oldKeyboardState.IsKeyUp(Keys.Enter))
            {
                LoadLevel();
            }
            if (keyboardState.IsKeyDown(Keys.Escape) && oldKeyboardState.IsKeyUp(Keys.Escape))
            {
                gameState = GameState.LevelSelect;
            }
        }

        private void UpdateWon()
        {
            if (keyboardState.IsKeyDown(Keys.Space) && oldKeyboardState.IsKeyUp(Keys.Space) || keyboardState.IsKeyDown(Keys.Enter) && oldKeyboardState.IsKeyUp(Keys.Enter))
            {
                if (currentLevel < LevelData.highestLevel)
                {
                    currentLevel++;
                    LoadLevel();
                }
                else
                    gameState = GameState.LevelSelect;
            }
            if (keyboardState.IsKeyDown(Keys.Escape) && oldKeyboardState.IsKeyUp(Keys.Escape))
            {
                gameState = GameState.LevelSelect;
            }
        }

        protected override void Draw(GameTime gameTime)
        {
            spriteBatch.Begin();
            GraphicsDevice.Clear(Color.CornflowerBlue);

            switch (gameState)
            {
                case GameState.TitleScreen:
                    DrawTitleScreen();
                    break;
                case GameState.LevelSelect: DrawLevelSelect(); break;
                case GameState.LevelWon:
                    DrawLevel();
                    DrawLeftText(Color.Black, "Level Complete!");
                    DrawRightText(Color.DarkGray, "Press Space to Continue...");
                    break;
                case GameState.LevelLose:
                    DrawLevel();
                    DrawLeftText(Color.Black, "Try Again!");
                    DrawRightText(Color.DarkGray, "Press Space to retry.");
                    break;
                case GameState.Level:
                    DrawLevel();
                    switch (currentLevel)
                    {
                        case 1: DrawLeftText(Color.DarkGray, "Use colors to eliminate all blocks.", "Arrow keys adjust aim."); break;
                        case 2: DrawLeftText(Color.DarkGray, "Press R to restart level."); break;
                        case 3: DrawLeftText(Color.DarkGray, "Remaining balls are shown", "on bottom left of screen."); break;
                        default: break;
                    }
                    break;
                default: break;
            }

            base.Draw(gameTime);
            spriteBatch.End();
        }

        private void DrawTitleScreen()
        {
            spriteBatch.Draw(logo, new Vector2(30, 50), Color.White);
        }

        private void DrawLevelSelect()
        {
            spriteBatch.DrawString(msgFont, "Select Level", new Vector2(LevelData.screenWidth / 2 - 80, 50), Color.Black);

            spriteBatch.Draw(blockTexture, new Rectangle(LevelData.screenWidth / 2 - 150, LevelData.screenHeight / 2 - 150, 100, 100), Color.White);
            spriteBatch.Draw(blockTexture, new Rectangle(LevelData.screenWidth / 2 + 50, LevelData.screenHeight / 2 - 150, 100, 100), Color.White);
            spriteBatch.Draw(blockTexture, new Rectangle(LevelData.screenWidth / 2 - 150, LevelData.screenHeight / 2 + 50, 100, 100), Color.White);
            spriteBatch.Draw(blockTexture, new Rectangle(LevelData.screenWidth / 2 + 50, LevelData.screenHeight / 2 + 50, 100, 100), Color.White);
            spriteBatch.DrawString(msgFont, "1", new Vector2(LevelData.screenWidth / 2 - 110, LevelData.screenHeight / 2 - 122), Color.Black);
            spriteBatch.DrawString(msgFont, "2", new Vector2(LevelData.screenWidth / 2 + 90, LevelData.screenHeight / 2 - 122), Color.Black);
            spriteBatch.DrawString(msgFont, "3", new Vector2(LevelData.screenWidth / 2 - 110, LevelData.screenHeight / 2 + 78), Color.Black);
            spriteBatch.DrawString(msgFont, "4", new Vector2(LevelData.screenWidth / 2 + 90, LevelData.screenHeight / 2 + 78), Color.Black);

            spriteBatch.Draw(boxTexture, new Rectangle((int)(box.X * 200f + LevelData.screenWidth / 2 - 150), (int)(box.Y * 200f + LevelData.screenHeight / 2 - 150), 100, 100), new Rectangle(0, 0, 95, 95), Color.White);
        }

        private void DrawLevel()
        {
            foreach (Block b in blockList)
            {
                b.Draw(spriteBatch);
            }
            floor.Draw(spriteBatch);
            spriteBatch.Draw(cannonTexture, new Rectangle((int)cannonPosition.X, (int)cannonPosition.Y, cannonTexture.Width, cannonTexture.Height), new Rectangle(0, 0, cannonTexture.Width, cannonTexture.Height), Color.White, -1* cannonAngle, new Vector2(20, 35), SpriteEffects.None, .9f);
            spriteBatch.Draw(powerBar, new Vector2(160, 370), new Rectangle(0, (powerBar.Height / 8) * (powerInt - 1), powerBar.Width, powerBar.Height / 8), Color.White);
            for (int i = 0; i < ballList.Count; i++)
            {
                spriteBatch.Draw(ballTexture, new Rectangle(25, LevelData.screenHeight - (i * 50 + 100), 30, 30), LevelData.GetColor(ballList.ElementAt(i)));
            }
            spriteBatch.DrawString(regFont, "[esc]", new Vector2(20, 10), Color.Black);
        }

        /*       0
         *       ^
         *       |
         * 3 < -   - > 1
         *       |
         *       v
         *       2
        */
        public void Move(int direction)
        {
            if (!moving)
            {
                int x = 0, y = 0;
                switch (direction)
                {
                    case 0: y = -1; break;
                    case 1: x = 1; break;
                    case 2: y = 1; break;
                    case 3: x = -1; break;
                    default: return;
                };

                oldloc = new Vector2(box.X, box.Y);
                newloc = new Vector2(box.X + x, box.Y + y);

                if (newloc.X < 2 && newloc.Y < 2 && newloc.X >= 0 && newloc.Y >= 0)
                {
                    if (direction == 0 || direction == 3)
                        multiplier = -1;
                    else
                        multiplier = 1;

                    if (direction == 1 || direction == 3)
                        leftRight = true;
                    else
                        leftRight = false;

                    moving = true;
                }
            }
        }

        public void MoveBox(GameTime gameTime)
        {
            if (moving)
            {
                if (!set)
                {
                    startTime = gameTime.TotalGameTime;
                    set = true;
                }

                TimeSpan time = gameTime.TotalGameTime.Subtract(startTime);
                double t = time.Milliseconds + 1000 * time.Seconds + 60000 * time.Minutes;

                float temp = (float)((-1.0 * Math.Cos((t / 370.0) * Math.PI) + 1) / 2.0) * multiplier;
                
                if (leftRight)
                    box.X = oldloc.X + temp;
                else
                    box.Y = oldloc.Y + temp;

                if (t / 370.0 >= 1.0)
                {
                    box = new Vector2(newloc.X, newloc.Y);
                    set = false;
                    moving = false;

                    if (box.X == 0 && box.Y == 0)
                        currentLevel = 1;
                    else if (box.X == 1 && box.Y == 0)
                        currentLevel = 2;
                    else if (box.X == 0 && box.Y == 1)
                        currentLevel = 3;
                    else if (box.X == 1 && box.Y == 1)
                        currentLevel = 4;
                }
            }
        }

        protected void DrawLeftText(Color color, params String[] text)
        {
            // Calculate the horizontal center of the viewport.
            int xCenter = GraphicsDevice.Viewport.Width / 4;

            // Print text items, bottom to top.
            for (int i = text.Length - 1, yPos = GraphicsDevice.Viewport.Height / 2 - 20 - msgFont.LineSpacing; i >= 0; i--, yPos -= msgFont.LineSpacing)
            {
                int textWidth = (int)msgFont.MeasureString(text[i]).X;
                spriteBatch.DrawString(msgFont, text[i], new Vector2(xCenter - (textWidth / 2), yPos), color);
            }
        }

        protected void DrawRightText(Color color, params String[] text)
        {
            // Calculate the horizontal center of the viewport.
            int xCenter = 3 * GraphicsDevice.Viewport.Width / 4;

            // Print text items, bottom to top.
            for (int i = text.Length - 1, yPos = GraphicsDevice.Viewport.Height / 2 - 20 - msgFont.LineSpacing; i >= 0; i--, yPos -= msgFont.LineSpacing)
            {
                int textWidth = (int)msgFont.MeasureString(text[i]).X;
                spriteBatch.DrawString(msgFont, text[i], new Vector2(xCenter - (textWidth / 2), yPos), color);
            }
        }

        public bool OnCollision(Fixture fixtureA, Fixture fixtureB, FarseerPhysics.Dynamics.Contacts.Contact contact)
        {
            int indexA = -1, indexB = -1;
            for (int i = 0; i < blockList.Count; i++)
            {
                for (int j = 0; j < blockList.ElementAt(i).body.FixtureList.Count; j++)
                {
                    if (fixtureA.Equals(blockList.ElementAt(i).body.FixtureList.ElementAt(j)))
                        indexA = i;
                    if (fixtureB.Equals(blockList.ElementAt(i).body.FixtureList.ElementAt(j)))
                        indexB = i;
                }
            }
            
            if(indexA >= 0 && indexB >= 0)
                if (blockList.ElementAt(indexA).colorNum > 0 && blockList.ElementAt(indexA).colorNum == blockList.ElementAt(indexB).colorNum)
                {
                    if (indexA > indexB)
                    {
                        world.RemoveBody(blockList.ElementAt(indexA).body);
                        blockList.RemoveAt(indexA);
                        world.RemoveBody(blockList.ElementAt(indexB).body);
                        blockList.RemoveAt(indexB);
                    }
                    else
                    {
                        world.RemoveBody(blockList.ElementAt(indexB).body);
                        blockList.RemoveAt(indexB);
                        world.RemoveBody(blockList.ElementAt(indexA).body);
                        blockList.RemoveAt(indexA);
                    }
                }
            return true;
        }
    }
}
