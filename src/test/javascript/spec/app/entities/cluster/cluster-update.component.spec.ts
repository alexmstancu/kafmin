/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import ClusterUpdateComponent from '@/entities/cluster/cluster-update.vue';
import ClusterClass from '@/entities/cluster/cluster-update.component';
import ClusterService from '@/entities/cluster/cluster.service';

import BrokerService from '@/entities/broker/broker.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});

describe('Component Tests', () => {
  describe('Cluster Management Update Component', () => {
    let wrapper: Wrapper<ClusterClass>;
    let comp: ClusterClass;
    let clusterServiceStub: SinonStubbedInstance<ClusterService>;

    beforeEach(() => {
      clusterServiceStub = sinon.createStubInstance<ClusterService>(ClusterService);

      wrapper = shallowMount<ClusterClass>(ClusterUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          alertService: () => new AlertService(store),
          clusterService: () => clusterServiceStub,

          brokerService: () => new BrokerService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      // it('Should call update service on save for existing entity', async () => {
      //   // GIVEN
      //   const entity = { id: 123 };
      //   comp.cluster = entity;
      //   clusterServiceStub.update.resolves(entity);

      //   // WHEN
      //   comp.save();
      //   await comp.$nextTick();

      //   // THEN
      //   expect(clusterServiceStub.update.calledWith(entity)).toBeTruthy();
      //   expect(comp.isSaving).toEqual(false);
      // });

      it('Should call create service on save for new entity', async () => {
        // // GIVEN
        // const entity = {};
        // comp.cluster = entity;
        // clusterServiceStub.create.resolves(entity);

        // // WHEN
        // comp.save();
        // await comp.$nextTick();

        // // THEN
        // expect(clusterServiceStub.create.calledWith(entity)).toBeTruthy();
        // expect(comp.isSaving).toEqual(false);
      });
    });
  });
});
