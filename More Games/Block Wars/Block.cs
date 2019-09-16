using System;
using FarseerPhysics.Dynamics;
using FarseerPhysics.Factories;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace Block_Wars
{
    public class Block
    {
        const float unitToPixel = 100.0f;
        const float pixelToUnit = 1 / unitToPixel;

        public Body body;
        public Vector2 Position
        {
            get { return body.Position * unitToPixel; }
            set { body.Position = value * pixelToUnit; }
        }

        private Vector2 sizeVector;
        public Vector2 Size
        {
            get { return sizeVector * unitToPixel; }
            set { sizeVector = value * pixelToUnit; }
        }

        Texture2D texture;
        public int colorNum;
        Color color;
        bool rectangle;
        World world;

        public Block(World w, Texture2D tex, Vector2 size, Vector2 startPosition, int color, bool isRect)
        {
            world = w;
            if (isRect)
                body = BodyFactory.CreateRectangle(world, size.X * pixelToUnit, size.Y * pixelToUnit, 1);
            else
                body = BodyFactory.CreateCircle(world, size.Length() / 2 * pixelToUnit, 1);
            body.BodyType = BodyType.Dynamic;

            Size = size;
            Position = startPosition;
            texture = tex;
            colorNum = color;
            rectangle = isRect;
            this.color = LevelData.GetColor(colorNum);
        }

        public void Draw(SpriteBatch spriteBatch)
        {
            Vector2 scale;
            if (rectangle)
                scale = new Vector2(Size.X / (float)texture.Width, Size.Y / (float)texture.Height);
            else
                scale = new Vector2(Size.Length() / (float)texture.Width, Size.Length() / (float)texture.Height);
            spriteBatch.Draw(texture, Position, null, color, body.Rotation, new Vector2(texture.Width / 2.0f, texture.Height / 2.0f), scale, SpriteEffects.None, 0);
        }
    }
}