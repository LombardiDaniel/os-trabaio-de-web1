package services

import (
	"context"
	"errors"
	"io"
	"time"

	"github.com/minio/minio-go/v7"
)

// ObjectService defines the interface for object storage operations.
// It provides methods for uploading and downloading objects, as well as
// generating signed URLs for secure access and upload URLs for pre-signed uploads.
type ObjectService interface {
	// Upload uploads an object to the specified bucket and path.
	Upload(ctx context.Context, bucket string, path string, size int64, data io.Reader) error

	// Download retrieves an object from the specified bucket and path.
	Download(ctx context.Context, bucket string, path string) ([]byte, error)

	// SignedUrl generates a signed URL for accessing an object in the specified bucket and path.
	SignedUrl(ctx context.Context, bucket string, path string, exp time.Duration) (string, error)

	// UploadUrl generates a pre-signed URL for uploading an object to the specified bucket and path.
	UploadUrl(ctx context.Context, bucket string, path string, exp time.Duration) (string, error)
}

type ObjectServiceMinioImpl struct {
	client *minio.Client
}

func NewObjectServiceMinioImpl(client *minio.Client) ObjectService {
	return &ObjectServiceMinioImpl{
		client: client,
	}
}

func (s *ObjectServiceMinioImpl) Upload(ctx context.Context, bucket string, path string, size int64, data io.Reader) error {
	_, err := s.client.PutObject(ctx, bucket, path, data, size, minio.PutObjectOptions{})
	return errors.Join(err, errors.New("could not put obj"))
}

func (s *ObjectServiceMinioImpl) Download(ctx context.Context, bucket string, path string) ([]byte, error) {
	obj, err := s.client.GetObject(ctx, bucket, path, minio.GetObjectOptions{})
	if err != nil {
		return nil, errors.Join(err, errors.New("could not get obj"))
	}
	defer obj.Close()

	data, err := io.ReadAll(obj)
	if err != nil {
		return nil, errors.Join(err, errors.New("could not io.ReadAll(obj)"))
	}
	return data, nil
}

func (s *ObjectServiceMinioImpl) SignedUrl(ctx context.Context, bucket string, path string, exp time.Duration) (string, error) {
	url, err := s.client.PresignedGetObject(ctx, bucket, path, exp, nil)
	if err != nil {
		return "", errors.Join(err, errors.New("could not presign get obj"))
	}
	return url.String(), nil
}

func (s *ObjectServiceMinioImpl) UploadUrl(ctx context.Context, bucket string, path string, exp time.Duration) (string, error) {
	url, err := s.client.PresignedPutObject(ctx, bucket, path, time.Minute*5)
	if err != nil {
		return "", errors.Join(err, errors.New("could not presign put obj"))
	}
	return url.String(), nil
}
