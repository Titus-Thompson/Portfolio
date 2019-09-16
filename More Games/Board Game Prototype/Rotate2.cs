using UnityEngine;
using System.Collections;

public class Rotate2 : MonoBehaviour {
	
	// Use this for initialization
	void Start () {
		
	}
	
	private float speed = 3 * (float)(9.0/29.0);
	
	// Update is called once per frame
	void Update () {
		
		if (Input.GetButton ("Horizontal") && Input.GetAxisRaw("Horizontal") > 0){
			RotateLeft();
		} else if ( Input.GetButton ("Horizontal") && Input.GetAxisRaw("Horizontal") < 0) {
			RotateRight();
		}
	}
	
	void RotateLeft () {
		transform.Rotate (Vector3.forward * (speed*(-1)));
	}
	void RotateRight () {
		transform.Rotate (Vector3.forward * speed);
	}
}