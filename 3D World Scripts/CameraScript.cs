using UnityEngine;
using System.Collections;

public class CameraScript : MonoBehaviour {

	public float turnSpeed;

	private float height;
	private float distance;

	private GameObject player;
	public float degrees;

	// Use this for initialization
	void Start () {
		player = GameObject.Find ("obon");
		degrees = 0;
        height = 15;
		distance = 25;
	}
	
	// Update is called once per frame
	void Update () {
		bool keyA = Input.GetKey (KeyCode.A);
		bool keyD = Input.GetKey (KeyCode.D);

		if (keyA) {
			degrees += turnSpeed;
			degrees = degrees % 360f;
		}
		if (keyD) {
			degrees -= turnSpeed;
			degrees = degrees % 360f;
		}

		bool keyW = Input.GetKey(KeyCode.W);
		bool keyS = Input.GetKey(KeyCode.S);

		if (keyW)
		{
			height += 1;
		}
		if (keyS)
		{
			height -= 1;
		}

		bool keyQ = Input.GetKey (KeyCode.Q);
		bool keyE = Input.GetKey (KeyCode.E);

		if (keyQ) {
			distance += 1;
		}
		if (keyE) {
			distance -= 1;
		}

		transform.position = new Vector3 (player.transform.position.x + distance, player.transform.position.y + height, player.transform.position.z);
		transform.RotateAround (player.transform.position, Vector3.up, degrees);
		transform.LookAt (player.transform);
	}
}
