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

resource "aws_iam_user" "sundews_github_actions_ci" {
  name = "sundews_github_actions"
}

resource "aws_iam_access_key" "sundews_github_actions_ci" {
  user = aws_iam_user.sundews_github_actions_ci.name
}
