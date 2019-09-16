using UnityEngine;
using System.Collections;

public class Movement : MonoBehaviour {

	public float speed;
	public float jumpForce;
    public float maxVelocity;
	private Rigidbody rb;
	private GameObject cam;
	private CameraScript camScript;

	// Use this for initialization
	void Start () {
		rb = GetComponent<Rigidbody>();
		cam = GameObject.Find("Main Camera");
		camScript = cam.GetComponent<CameraScript>();
	}
	
	// Update is called once per frame
	void FixedUpdate () {
		float moveHorizontal = Input.GetAxis ("Horizontal");
		float moveVertical = Input.GetAxis ("Vertical");
		bool keyZ = Input.GetKey (KeyCode.Z);
		bool space = Input.GetKey (KeyCode.Space);

        if (rb.velocity.magnitude <= maxVelocity)
        {
            Vector3 movement = new Vector3(moveHorizontal, 0.0f, moveVertical);
            movement = Quaternion.AngleAxis(camScript.degrees - 90, Vector3.up) * movement;
            rb.AddForce(movement * speed);

            if (space)
            {
                rb.AddForce(Vector3.up * jumpForce);
            }
        }

		if (keyZ) {
			rb.angularVelocity = new Vector3(0,0,0);
		}
	}
}
