# SOA-FaaS-Tutorial

## Introduction to FaaS and AWS Lambda

### What is FaaS?
**Function-as-a-Service (FaaS)** is a cloud computing model that allows developers to run individual functions in response to events without managing servers. FaaS automatically scales based on demand, reducing costs and operational overhead. These functions are **event-driven**, meaning they are triggered by specific actions like HTTP requests, database updates, or message queue events.

### What is AWS Lambda?
**AWS Lambda** is Amazon's FaaS offering that allows developers to run code in response to events. It supports multiple languages (Python, Node.js, Java, etc.) and integrates seamlessly with other AWS services like **API Gateway, DynamoDB, S3, and SNS**.

#### Why Use AWS Lambda?
- **Automatic scaling** – AWS handles scaling based on request volume.
- **Cost-efficient** – No charges when the function is idle.
- **Easy integration** – Works with AWS services for building serverless applications.

## Steps to Create an AWS Lambda Function

### Step 1: Sign in to AWS Management Console
1. Go to the [AWS Management Console](https://aws.amazon.com/console/).
2. Search for **Lambda** in the AWS services search bar.
![image](https://github.com/user-attachments/assets/d945af11-5ff7-4e46-873b-f417cf7ceab3)
4. Click on **AWS Lambda** to open the service.

### Step 2: Create a New Lambda Function
1. Click on **Create function**.
![image](https://github.com/user-attachments/assets/077ae873-9ea4-4270-a95b-eabdebe3f0f9)
3. Choose **Author from scratch**.
![image](https://github.com/user-attachments/assets/b36b864d-f0bf-4701-9bef-9beb1e5151db)
5. Enter a function name (e.g., `deadline_reminder_2`).
6. Select a runtime (e.g., **Python 3.9**, **Node.js 14**, etc.).
7. Choose or create an **Execution role** with necessary permissions.
![image](https://github.com/user-attachments/assets/c394b173-046a-4925-8eaf-122593820f97)
9. Click **Create function**.
![image](https://github.com/user-attachments/assets/122b0d47-6aa5-4e22-81e6-429cba32cf7f)


### Step 3: Write the Function Code
1. In the **Function code** section, use the built-in editor or upload a zip file.
2. In the editor you can write the function you need, but for this tutorial I prepared a function that processes incoming JSON requests to calculate the number of days remaining until a specified due date. If the due date is within 3 days, it returns a reminder message. Otherwise, it provides the number of days left. It also includes error handling for missing or invalid inputs.
3. Example Python function:
   
```python
import json
import datetime

def lambda_handler(event, context):
    try:
        # Parse input JSON
        body = json.loads(event["body"]) if "body" in event else event
        due_date_str = body.get("dueDate")
        task_title = body.get("title")
        
        if not due_date_str:
            return {
                "statusCode": 400,
                "body": json.dumps({"error": "Missing 'dueDate' field"})
            }
        
        if not task_title:
            return {
                "statusCode": 400,
                "body": json.dumps({"error": "Missing 'title' field"})
            }
        
        # Convert string to date
        due_date = datetime.datetime.strptime(due_date_str, "%Y-%m-%d").date()
        today = datetime.date.today()
        
        # Calculate remaining days
        remaining_days = (due_date - today).days
        
        # Prepare response
        response = {
            "taskTitle": task_title,
            "remainingDays": remaining_days
        }
        
        # Add reminder only if remaining days are <= 3
        if remaining_days <= 3:
            response["reminder"] = "Your task '{}' is due in {} days.".format(task_title, remaining_days)
        else:
            response["reminder"] = ""
            
        return {
            "statusCode": 200,
            "body": json.dumps(response)
        }
    
    except Exception as e:
        return {
            "statusCode": 500,
            "body": json.dumps({"error": str(e)})
        }
   ```
4. Once you wrote the function you wanted, you click **Deploy** to save the changes.
5. Now, we need a way to send the request to this AWS Lambda, but what is the endpoint for it? In order to generate a Function URL (endpoint), we need to go to **Configuration** -> **Function URL** -> **Create function URL**. There you will be asked to choose an Auth type. For this tutorial, the auth type selected was: **NONE**.
![image](https://github.com/user-attachments/assets/da5d81b5-ce08-41df-b1b5-fcb2d58ab825)
![image](https://github.com/user-attachments/assets/47b0bdbd-cca1-4ca9-bca1-bcc906818941)


### Step 4: Test the Function
1. Before integrating this function into an application, it is essential to test it. The testing can be done either directly from AWS, by going to **Test** section, or it can be tested using Postman.

## AWS testing:
![image](https://github.com/user-attachments/assets/7d9d8fef-91f0-448b-8dcf-6a435bda3598)
![image](https://github.com/user-attachments/assets/cc74af25-b9df-475a-a100-1a9531dd45bd)

## Postman testing:
![image](https://github.com/user-attachments/assets/f49abe18-bcaf-485e-a967-35bf75b0b4e6)

### Step 5: Integrating the function in a Java Spring Boot application

1. Now that we know the function is doing what we are expecting, we need to integrate it in our application.
2. For this, we are going to define in **application.yaml* the function URL, and create a simple HTTP request.
![image](https://github.com/user-attachments/assets/8fb8eb58-98a3-4d0a-bad4-934fb9f71730)
3. For this we will use a rest template. We will create the headers necessary for the request, set them and create the http entity.
4. To execute the function, we use the rest template with .exchange(...), where we provide the http entity, the lambda url, the type of http request (e.g. POST) and the expected output.
5. Return the result from the execution of lambda function and finally create an endpoint in the controller. Run the spring boot application and test the endpoint using Postman.
![image](https://github.com/user-attachments/assets/9cf59424-4a6e-4260-bbab-ac380c87b892)
![image](https://github.com/user-attachments/assets/fa97edc4-b9ab-41eb-a238-bf72bf561341)

Now you are good to go and start implement your own AWS Lambda function.

Other aspects:
1. If you want to investigate the logs for the AWS lambda function, AWS CloudWatch can be used.

I hope you enjoyed this :smile:
