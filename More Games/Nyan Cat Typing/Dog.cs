
//----------------------------------------------------------------------//
// Dog.cs
//----------------------------------------------------------------------//

using UnityEngine;
using System.Collections;

public class Dog {
	
	public SpriteController controller;
	public GameObject toaster;
	
	public string word;
	public int lettersTyped;
	public bool alive;
	
	private float speed;
	private Vector2 pos;
	
	private Camera cam;
	public int i;
	private int l;
	
	// Use this for initialization
	public Dog (GameObject t, int level, Camera c, int index, string typeWord) {
	
		alive = true;
		toaster = t;
		word = typeWord;
		controller = toaster.GetComponent<SpriteController>();
		controller.setScale(0.46f, 0.23f);
		controller.age += 0.3f * Random.value;
		cam = c;
		i = index;
		
		speed = level * 0.02f;
		l = level;
		pos = new Vector2(0,0);
		
	}
	
	public void SetPosition()
	{
		if (l <= 12)
			pos.x = -0.5f * (i / 3) + controller.age * speed - 0.13f * (l - 2);
		else
			pos.x = -0.5f * (i / 3) + controller.age * speed - 0.13f * 10;
		pos.y = 0.35f * (i % 3) - 0.32f + 0.25f * Mathf.Cos (controller.age);
		controller.setPosition (new Vector3((float)cam.gameObject.transform.position.x + pos.x,
			(float)cam.gameObject.transform.position.y + pos.y, cam.gameObject.transform.position.z + 1.9f));
	}
	
	// Update is called once per frame
	public void Update ()
	{
		if(alive)
			SetPosition ();
	}
	
	public void DrawWord(GUIStyle style)
	{
		Vector2 position = Camera.main.WorldToViewportPoint(new Vector3(pos.x, pos.y, 0));
		position.x = Screen.width * Camera.main.rect.x + Screen.width * Camera.main.rect.width * position.x;
		position.y = Screen.height - Screen.height * position.y;
		position.y = Screen.height * Camera.main.rect.y + Camera.main.rect.height * position.y;
		position.x -= Screen.width * Camera.main.rect.width * (float)(1.0 / 15);
		position.y += Screen.height * Camera.main.rect.height * (float)(1.0 / 18);
		GUI.contentColor = Color.white;
		GUI.Label(new Rect(position.x, position.y, 110, 30), word, style);
	}
	
	public void Kill()
	{
		GameObject.Destroy (toaster);
		alive = false;
	}
	
}
