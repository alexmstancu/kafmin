/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import TopicUpdateComponent from '@/entities/topic/topic-update.vue';
import TopicClass from '@/entities/topic/topic-update.component';
import TopicService from '@/entities/topic/topic.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('Topic Management Update Component', () => {
    let wrapper: Wrapper<TopicClass>;
    let comp: TopicClass;
    let topicServiceStub: SinonStubbedInstance<TopicService>;

    beforeEach(() => {
      topicServiceStub = sinon.createStubInstance<TopicService>(TopicService);

      wrapper = shallowMount<TopicClass>(TopicUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          topicService: () => topicServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {

      });

    });
  });
});
