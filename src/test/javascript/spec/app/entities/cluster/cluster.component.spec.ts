/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import AlertService from '@/shared/alert/alert.service';
import * as config from '@/shared/config/config';
import ClusterComponent from '@/entities/cluster/cluster.vue';
import ClusterClass from '@/entities/cluster/cluster.component';
import ClusterService from '@/entities/cluster/cluster.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-alert', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Cluster Management Component', () => {
    let wrapper: Wrapper<ClusterClass>;
    let comp: ClusterClass;
    let clusterServiceStub: SinonStubbedInstance<ClusterService>;

    beforeEach(() => {
      clusterServiceStub = sinon.createStubInstance<ClusterService>(ClusterService);
      clusterServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ClusterClass>(ClusterComponent, {
        store,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          alertService: () => new AlertService(store),
          clusterService: () => clusterServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      clusterServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllClusters();
      await comp.$nextTick();

      // THEN
      expect(clusterServiceStub.retrieve.called).toBeTruthy();
      expect(comp.clusters[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      clusterServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeCluster();
      await comp.$nextTick();

      // THEN
      expect(clusterServiceStub.delete.called).toBeTruthy();
      expect(clusterServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
