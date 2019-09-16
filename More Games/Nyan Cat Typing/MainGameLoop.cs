
//----------------------------------------------------------------------//
// MainGameLoop.cs
//----------------------------------------------------------------------//

using UnityEngine;
using System.Collections;

public class MainGameLoop : GameLoop {
	
	// Sprite Prototypes
	public GameObject nyanPrototype;
	public GameObject toasterPrototype;
	private Dog[] toasters, activeToasters;
	static int numDogs = 12;
	
	// Actual Sprites
	SpriteController nyanController;
	
	//Word stuff
	string textEntered;
	static string[] wordList = {
		"nyan",
		"cat",
		"toaster",
		"dog",
		"pop",
		"tart",
		"eat",
		"cook",
		
		"space",
		"stars",
		"sparkle",
		"flying",
		"run",
		"chase",
		
		"fun",
		"meme",
		"internet",
		"youtube",
		"catchy",
		"annoying",

		"colors",
		"red",
		"orange",
		"yellow",
		"green",
		"blue",
		"purple",
		"rainbow"
	};
	
	//State Data
	enum State
	{
		Level,
		Complete,
		GetReady,
		GameOver,
		Pause
	};
	State currentState;
	State unpauseState;
	int level;
	
	//Music
	bool audioStarted;

	//ParticleSystem
	ParticleSystem particles;

	//Timer
	float totalTime;
	int totalWords;
		

	//----------------------------------------------------------------------//
	// Initialization
	//----------------------------------------------------------------------//

	public override void Start () 
	{
		//base.Start();

		//setCameraPosition (new Vector(0.0f, 0.0f, -2.0f));
		cam.aspect = 16.0f/9.0f;
		
		// Load the descriptions of the prototypes from text files
		if (nyanPrototype) {
			SpriteController c = nyanPrototype.GetComponent<SpriteController>();
			c.loadFromFile("NyanCat");
			
			GameObject nyanCat = instantiateSprite (nyanPrototype, new Vector3(Screen.width/2, Screen.height/2, 0));
			nyanController = nyanCat.GetComponent<SpriteController>();
			nyanController.setScale(3.5f, 0.2f);
			nyanController.setPosition (new Vector3(cam.gameObject.transform.position.x - nyanController.animations[0].frameSize.x + 0.6f,
				cam.gameObject.transform.position.y, cam.gameObject.transform.position.z + 2));
		}
		
		toasters = new Dog[numDogs];
		
		level = 1;
		currentState = State.Complete;
		
		audioStarted = false;

		if (toasterPrototype)
		{
			SpriteController c = toasterPrototype.GetComponent<SpriteController>();
			c.loadFromFile ("Toaster");

			for(int i = 0; i < numDogs; i++)
			{
				GameObject toaster = instantiateSprite (toasterPrototype, new Vector3(Screen.width/2, Screen.height/2, 0));
				toasters[i] = new Dog(toaster, level, cam, i, "null");
				toasters[i].alive = false;
			}
		}

		particles = GameObject.Find("Particle System").GetComponent<ParticleSystem>();
		particles.emissionRate = 0;

		totalTime = 0;
		totalWords = 0;
	}
	
	//----------------------------------------------------------------------//
	// GUI Stuff - Drawing text and buttons
	//----------------------------------------------------------------------//
	
	public override void OnGUI() 
	{
		setGUIstyles();
		
		Vector2 position = new Vector2();		//generic vector2 for drawing purposes
		
		if(currentState != State.Pause)
		{
			position.x = Screen.width * Camera.main.rect.x + 10;
			position.y = Screen.height * Camera.main.rect.y + 10;
			GUI.Label (new Rect(position.x, position.y, 50, 30), "esc", labelStyle);
		}
		
		if (currentState == State.Level)
		{
			for (int i = 0; i < activeToasters.Length; i++)
			{
				if (activeToasters[i].alive)
					activeToasters[i].DrawWord(labelStyle);
			}
			position.x = Screen.width - Camera.main.rect.x * Screen.width;
			position.y = Screen.height * Camera.main.rect.y;
			position.x -= Screen.width * Camera.main.rect.width * (float)(7.0 / 30);
			position.y += Screen.height * Camera.main.rect.height * (float)(1.0 / 30);
			GUI.contentColor = Color.white;
			GUI.Label(new Rect(position.x, position.y, 175, 40), textEntered, labelStyleLarge);
		}
		else if (currentState == State.Complete)
		{
			if (level == 1)
			{
				position.x = Screen.width / 2 - Screen.width * Camera.main.rect.width * (float)(3.0 / 16);
				position.y = Screen.height / 2 - Screen.height * Camera.main.rect.height * (float)(1.0 / 4);
				GUI.Label (new Rect(position.x, position.y, Screen.width * Camera.main.rect.width * (float)(6.0 / 16),
					30), "Press any key to start.", labelStyle);
			}
			else
			{
				position.x = Screen.width / 2 - Screen.width * Camera.main.rect.width * (float)(2.0 / 16);
				position.y = Screen.height / 2 - Screen.height * Camera.main.rect.height * (float)(1.0 / 4);
				GUI.Label (new Rect(position.x, position.y, Screen.width * Camera.main.rect.width * (float)(4.0 / 16),
					45), "Complete!", labelStyleLarge);
				position.x = Screen.width / 2 - Screen.width * Camera.main.rect.width * (float)(3.0 / 16);
				position.y = Screen.height / 2 + Screen.height * Camera.main.rect.height * (float)(1.0 / 4);
				GUI.Label (new Rect(position.x, position.y, Screen.width * Camera.main.rect.width * (float)(6.0 / 16),
					30), "Press any key to continue.", labelStyle);
				
			}
		}
		else if (currentState == State.GetReady)
		{
			position.x = Screen.width / 2 - Screen.width * Camera.main.rect.width * (float)(2.0 / 16);
			position.y = Screen.height / 2 - Screen.height * Camera.main.rect.height * (float)(1.0 / 4);
			GUI.Label (new Rect(position.x, position.y, Screen.width * Camera.main.rect.width * (float)(4.0 / 16),
				45), "Level " + level, labelStyleLarge);
		}
		else if (currentState == State.Pause)
		{
			position.x = Screen.width * Camera.main.rect.x + 10;
			position.y = Screen.height * Camera.main.rect.y + 10;
			GUI.Label (new Rect(position.x, position.y, Screen.width * Camera.main.rect.width - 20, Screen.height * Camera.main.rect.height - 20), 
				"\nPause\n\n\nCopyright 2013 Titus Thompson\n\nMusic taken from www.nyan.cat\n" +
				"All arwork created from scratch.\n\n\n\nPress any key to return to game.", labelStyleLarge);
		}
		else if (currentState == State.GameOver)
		{
			position.x = Screen.width / 2 - Screen.width * Camera.main.rect.width * (float)(4.0 / 16);
			position.y = Screen.height / 2 - Screen.height * Camera.main.rect.height * (float)(1.0 / 4);
			GUI.Label (new Rect(position.x, position.y, Screen.width * Camera.main.rect.width * (float)(8.0 / 16),
				45), "Game Over: Level " + level, labelStyleLarge);
			position.x = Screen.width / 2 - Screen.width * Camera.main.rect.width * (float)(3.0 / 32);
			position.y = Screen.height / 2 + Screen.height * Camera.main.rect.height * (float)(1.0 / 8);
			GUI.Label (new Rect(position.x, position.y, Screen.width * Camera.main.rect.width * (float)(6.0 / 32),
			                    30), "wpm: " + Mathf.Round (totalWords / (totalTime / 60)), labelStyle);
			position.x = Screen.width / 2 - Screen.width * Camera.main.rect.width * (float)(3.0 / 16);
			position.y = Screen.height / 2 + Screen.height * Camera.main.rect.height * (float)(1.0 / 4);
			GUI.Label (new Rect(position.x, position.y, Screen.width * Camera.main.rect.width * (float)(6.0 / 16),
				30), "Press enter to continue.", labelStyle);
		}
	}
	
	//----------------------------------------------------------------------//
	// Update - the main game loop, called once per frame
	//----------------------------------------------------------------------//
	
	public override void Update () 
	{
		base.Update();
		
		if (Input.GetKeyDown (KeyCode.Escape) && currentState != State.Pause)
		{
			unpauseState = currentState;
			currentState = State.Pause;
			particles.Pause();
			nyanController.doAnimate = false;
			for (int i = 0; i < toasters.Length; i++)
			{
				toasters[i].controller.doAnimate = false;
			}
		}
		else if (currentState == State.Level)
		{
			totalTime += Time.fixedDeltaTime;
			for (int i = 0; i < toasters.Length; i++)
			{
				toasters[i].Update ();
			}
			
			if (Input.inputString != "" && !Input.GetKeyDown(KeyCode.Return) && !Input.GetKeyDown(KeyCode.Space) && !Input.GetKeyDown(KeyCode.Backspace))
				textEntered += Input.inputString;
			if (Input.GetKeyDown (KeyCode.Backspace) && textEntered.Length > 0)
				textEntered = textEntered.Substring(0, textEntered.Length - 1);
			
			bool allDead = true, nyanDead = false;
			for(int i = 0; i < 3; i++)
			{
				if (activeToasters[i].alive)
				{
					allDead = false;
					if (textEntered == activeToasters[i].word)
					{
						activeToasters[i].Kill ();
						if (activeToasters[i].i + 3 < numDogs)
							activeToasters[i] = toasters[activeToasters[i].i + 3];
						textEntered = "";
						totalWords++;
					}
					
					if(activeToasters[i].controller.getPosition().x > nyanController.getPosition().x + 1.3f)
						nyanDead = true;
				}
			}
			
			if (allDead)
			{
				currentState = State.Complete;
				level++;
			}
			
			if (nyanDead)
			{
				for(int j = 0; j < numDogs; j++)
					if (toasters[j].alive)
						toasters[j].Kill ();
				currentState = State.GameOver;
				textEntered = "";
			}
		}
		else if (currentState == State.Complete)
		{
			if (Input.inputString != "")
			{
				currentState = State.GetReady;
				GameObject.Find ("NyanMusic").GetComponent<AudioSource>().Stop ();
			}
		}
		else if (currentState == State.GetReady)
		{
			if (!audioStarted)
			{
				Camera.main.GetComponent<AudioSource>().Play();
				audioStarted = true;
			}
			else if (!Camera.main.GetComponent<AudioSource>().isPlaying)
			{
				audioStarted = false;
				LoadLevel ();
				GameObject.Find ("NyanMusic").GetComponent<AudioSource>().Play ();
				GameObject.Find("NyanMusic").GetComponent<AudioSource>().loop = true;
			}
		}
		else if (currentState == State.Pause)
		{
			if (Input.inputString != "" || Input.GetKeyDown (KeyCode.Escape))
			{
				currentState = unpauseState;
				nyanController.doAnimate = true;
				for (int i = 0; i < toasters.Length; i++)
				{
					toasters[i].controller.doAnimate = true;
				}
				particles.Play();
			}
		}
		else if (currentState == State.GameOver)
		{
			if (Input.GetKeyDown (KeyCode.Return))
			{
				level = 1;
				particles.emissionRate = 0;
				GameObject.Find ("NyanMusic").GetComponent<AudioSource>().Stop ();
				currentState = State.GetReady;
				totalTime = 0;
				totalWords = 0;
			}
		}
	}
	
	public void LoadLevel()
	{
		if(toasterPrototype)
		{
			SpriteController c = toasterPrototype.GetComponent<SpriteController>();
			c.loadFromFile("Toaster");
			
			string word = "a";
			for (int i = 0; i < toasters.Length; i++)
			{
				bool same;
				do
				{
					same = false;
					int r = (int)Mathf.Round (Random.Range (0, wordList.Length));
					word = wordList[r];
					for (int j = 0; j < i; j++)
					{
						if (word == toasters[j].word)
							same = true;
					}
				} while (same);

				GameObject toaster = instantiateSprite (toasterPrototype, new Vector3(Screen.width/2, Screen.height/2, 0));
				toasters[i] = new Dog(toaster, level, cam, i, word);
				toasters[i].SetPosition();
			}
			activeToasters = new Dog[3];
			activeToasters[0] = toasters[0];
			activeToasters[1] = toasters[1];
			activeToasters[2] = toasters[2];
		}
		particles.emissionRate = level * 8;
		currentState = State.Level;
	}

}
