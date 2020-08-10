import axios from 'axios';

import { ITopic } from '@/shared/model/topic.model';

const baseApiUrl = 'api/topics';

export default class TopicService {
  public find(clusterDbId: number, topicName: number): Promise<ITopic> {
    return new Promise<ITopic>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/${clusterDbId}/${topicName}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public retrieve(): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(baseApiUrl)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public delete(clusterDbId: number, topicName: string): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .delete(`${baseApiUrl}/${clusterDbId}/${topicName}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public create(clusterDbId: number, entity: ITopic): Promise<ITopic> {
    return new Promise<ITopic>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}/${clusterDbId}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public update(entity: ITopic): Promise<ITopic> {
    return new Promise<ITopic>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
