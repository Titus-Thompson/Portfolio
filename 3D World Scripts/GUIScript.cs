using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GUIScript : MonoBehaviour
{
	private int collected;
    public Texture icon;
    public Font font;
    public int[] s;
    GUIStyle style;

    // Start is called before the first frame update
    void Start()
    {
        collected = 0;
        s = new int[] {0, 0, 0, 0, 0};
        style = new GUIStyle();
        style.fontSize = 64;
        style.alignment = TextAnchor.MiddleRight;
        style.normal.textColor = Color.white;
    }

    // Update is called once per frame
    void OnGUI()
    {
        GUI.Label(new Rect(Screen.width - 200, 35, 90, 56), collected + " / 5", style);
		GUI.DrawTexture (new Rect(Screen.width - 100, 30, 64, 64), icon, ScaleMode.ScaleToFit, true);
    }

    public void RefreshStarCount()
    {
        collected = 0;
        foreach (int star in s)
        {
            if (star == 1)
                collected++;
        }
    }
}
