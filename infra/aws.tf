terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.27"
    }
  }

  required_version = ">= 0.14.9"
}

provider "aws" {
  profile = "default"
  region  = "us-west-1"
}

resource "aws_instance" "sundews_web_server_1" {
  ami           = "ami-0c8b7aacccd254467" // ECS-optimized Amazon Linux 2
  instance_type = "t2.micro"
}

resource "aws_ecr_repository" "sundews_container_repository" {
  name                 = "sundews_container_repo"
  image_tag_mutability = "IMMUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }
}

resource "aws_iam_group" "sundews_group" {
  name = "sundews"
}

resource "aws_iam_user" "sundews_github_actions_ci" {
  name = "sundews_github_actions_ci"
}

resource "aws_iam_access_key" "sundews_github_actions_ci" {
  user = aws_iam_user.sundews_github_actions_ci.name
}

output "sundews_github_actions_ci_iam_secret" {
  sensitive = true
  value     = aws_iam_access_key.sundews_github_actions_ci.secret
}

resource "aws_iam_user" "sundews_terraform" {
  name = "sundews_terraform"
}

resource "aws_iam_access_key" "sundews_terraform" {
  user = aws_iam_user.sundews_terraform.name
}

output "sundews_terraform_iam_secret" {
  sensitive = true
  value     = aws_iam_access_key.sundews_terraform.secret
}

resource "aws_iam_group_membership" "sundews_group" {
  name = "sundews-membership"

  users = [
    aws_iam_user.sundews_github_actions_ci.name,
    aws_iam_user.sundews_terraform.name,
  ]

  group = aws_iam_group.sundews_group.name
}

resource "aws_iam_group_policy" "sundews_group_policy" {
  name  = "sundews_group_policy"
  group = aws_iam_group.sundews_group.name

  policy = jsonencode({
    "Version" : "2012-10-17",
    "Statement" : [
      {
        "Effect" : "Allow",
        "Action" : "*",
        "Resource" : "*"
      }
    ]
  })
}
