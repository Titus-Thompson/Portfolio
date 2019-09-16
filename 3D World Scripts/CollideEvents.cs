using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CollideEvents : MonoBehaviour
{
    private GameObject[] stars;
    private GUIScript guiScript;

    // Start is called before the first frame update
    void Start()
    {
        guiScript = GameObject.Find("Main Camera").GetComponent<GUIScript>();

        stars = new GameObject[5];
        stars[0] = GameObject.Find("Star1");
        stars[1] = GameObject.Find("Star2");
        stars[2] = GameObject.Find("Star3");
        stars[3] = GameObject.Find("Star4");
        stars[4] = GameObject.Find("Star5");
    }

    // Update is called once per frame
    void OnCollisionEnter(Collision collision)
    {
        GameObject obj = collision.collider.gameObject;
        for (int i = 0; i < stars.Length; i++)
        {
            if (obj == stars[i])
            {
                guiScript.s[i] = 1;
                guiScript.RefreshStarCount();

                stars[i].GetComponent<ParticleSystem>().Play();
            }
        }
    }
}
